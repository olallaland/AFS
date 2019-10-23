package main.java.fileControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import main.java.application.WriteFileCmd;
import main.java.blockControl.BlockImpl;
import main.java.blockControl.BlockManagerImpl;
import main.java.constant.FileConstant;
import main.java.util.FileUtil;

public class FileImpl implements File {
	String fileData;
	FileMeta fileMeta;
	long pointer = 0;
	
	public FileImpl(FileMeta fileMeta) {
		this.fileMeta = fileMeta;
	}
	@Override
	public Id getFileId() {
		return fileMeta.fileId;
	}

	@Override
	public FileManager getFileManager() {
		return new FileManagerImpl(fileMeta.fmId);
	}

	@Override
	public byte[] read(int length) {
		//文件的所有字节
		byte[] total = new byte[(int)size()];
		//用户想要读取的字节
		System.out.println("yonghuxiangyaoduqudezijieshu: " + (int)(size() - pointer));
		byte[] result = new byte[(int) (size() - pointer)];
		HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = fileMeta.getLogicBlocks();
		for(Integer i : logicBlocks.keySet()) {
			LinkedList<BlockImpl> duplicates = logicBlocks.get(i);
			byte[] tempContent = null;
			for(int j = 0; j < duplicates.size(); j++) {
				BlockImpl block = duplicates.get(j);
				if(block.isValid()) {
					tempContent = FileUtil.reads(block.getPath() + FileConstant.DATA_SUFFIX);
				} else {
					duplicates.remove(j);
					continue;
				}	
				tempContent = FileUtil.reads(block.getPath() + FileConstant.DATA_SUFFIX);	
			}
			
			//如果该logicBlock下的所有block都无效，那么为文件空洞
			if(tempContent.length == 0) {
				tempContent = new byte[FileConstant.BLOCK_SIZE];
			}
			//拼接每个block的内容
			System.arraycopy(tempContent, 0, total, i * FileConstant.BLOCK_SIZE, tempContent.length);
		}
		System.arraycopy(total, (int) pointer, result, 0, (int) (size() - pointer));
		return result;
	}

	@Override
	public void write(byte[] bytes) {
		//1. 先将pointer之前那部分内容写到新分配的block里
		//2. 从上一步的末尾开始写入新的内容
		//3. 如果新的内容的size < （之前内容的size - pointer），
		//那么将剩余的这部分接着写到新的block中
		//！！！将之前filedata读出来，然后掐头去尾进行拼接，再写
		//1. 如果pointer大于filesize，那么pointer-filesize为需要填充0x00的部分，
		//其中第一部分为最后一个block的空白size；第二部分为整个block为空，
		//第三部分为一个block前面一定的size为空，然后开始写内容
		//2. 如果pointer小于filesize，那么如果其不为blocksize的整数倍，
		//那么从从一个block的中部开始写，先将数据读出来，从指定位置开始改变blockdata再写回；
		//在这种情况下，如果contentsize<filesize-pointer,那么还将剩余部分复制到content的末尾
		long fileSize = fileMeta.fileSize;
		HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = fileMeta.getLogicBlocks();
		LinkedList<BlockImpl> duplicates;
		int blockCount = fileMeta.blockCount;
		if(pointer >= fileMeta.fileSize) {
			
			long fileHoleCount = pointer - fileSize;
			System.out.println("空洞: " + fileHoleCount);
			//情况一： content的内容直接添加在最后一个block里
			//读取该block的内容，然后拷贝到另一个block
			if(fileSize % FileConstant.BLOCK_SIZE != 0) {
				System.out.println("进入了正确的方向！");
				BlockImpl lastBlock = logicBlocks.get(blockCount - 1).get(0);//这里还需检查block是否valid
				byte[] temp = lastBlock.read();
				System.out.println("the last block size is(should be less than 8): " + temp.length);
				//
				byte[] large = new byte[(int) (temp.length + fileHoleCount + bytes.length)];
				//将最后一个block的内容复制到大数组的开头
				System.arraycopy(temp, 0, large, 0, temp.length);
				//添加文件空洞的内容
				for(int i = temp.length; i < fileHoleCount + temp.length; i++) {
					large[i] = 0x00;
				}
				//复制要写入的内容到大数组的最后一部分
				System.arraycopy(bytes, 0, large, (int) (temp.length + fileHoleCount), bytes.length);
				
				//large数组的内容是新文件block index从blockCount - 1开始的内容
				subWrite(large, blockCount - 1);
			} else if(fileSize % FileConstant.BLOCK_SIZE == 0){ //第一次向文件写入数据属于此类
				byte[] large = new byte[(int) (fileHoleCount + bytes.length)];
				//添加文件空洞的内容
				for(int i = 0; i < fileHoleCount; i++) {
					large[i] = 0x00;
				}
				//复制要写入的内容到大数组的最后一部分
				System.arraycopy(bytes, 0, large, (int)fileHoleCount, bytes.length);
				subWrite(large, blockCount);
			}
		}
		
	}
	
	void subWrite(byte[] bytes, int beginBlock) {
		int position = 0;
		String filename = "";
		String content = "";
		BlockManagerImpl bm;
		int blockCount = fileMeta.blockCount;
		long fileSize = fileMeta.fileSize;
		
		int newBlockCount = (bytes.length % FileConstant.BLOCK_SIZE) == 0
				? (bytes.length / FileConstant.BLOCK_SIZE)
				: (bytes.length / FileConstant.BLOCK_SIZE) + 1;
		System.out.println("new block count : " + newBlockCount);
		
		for(int i = 0; i < newBlockCount; i++) {
			LinkedList<BlockImpl> duplicates = new LinkedList<BlockImpl>();
			byte[] temp;
			int begin = i * FileConstant.BLOCK_SIZE;
			int end = (i + 1) * FileConstant.BLOCK_SIZE;
			//System.out.println("copy begins at: " + begin + "\n ends at: " + end);
			if(end > bytes.length) {
				temp = Arrays.copyOfRange(bytes, begin, bytes.length);			
			} else {
				temp = Arrays.copyOfRange(bytes, begin, end);
			}		
			System.out.println("写入的字节: " + new String(temp));
			bm = WriteFileCmd.allocBm();
			BlockImpl block = (BlockImpl) bm.newBlock(temp);
			try {
				duplicates.add(block);
				//修改fileMeta的logicBlock块
				fileMeta.setLogicBlocks(i + beginBlock, duplicates);
			} catch(Exception e) {
				System.out.println("Add block to fileMeta error : " + e);
			}
		}
		
		fileMeta.setFileSize(fileMeta.fileSize + bytes.length - fileMeta.fileSize % FileConstant.BLOCK_SIZE);
		//这里有问题！！！
		fileMeta.setBlockCount(fileMeta.blockCount + newBlockCount);
		
		//4. 写入完成后，将fileMeta的内容写回对应文件
		try {
			fileMeta.write();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("写回filemeta时出错：" + e1);
		}
		try {
			
			fileMeta.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("读取fileMeta时发生错误：" + e);
		}

	}

	@Override
	public long move(long offset, int where) {
		switch(where) {
			case MOVE_HEAD:
				pointer = offset;
				break;
			case MOVE_CURR:
				pointer += offset;
				break;
			case MOVE_TAIL:
				pointer = size() + offset;
				break;
			default:
				throw new RuntimeException("输入的指针移动位置参数有误");
		}
		return 0;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return fileMeta.fileSize;
	}

	@Override
	public void setSize(long newSize) {
		// TODO Auto-generated method stub

	}

}
