package main.java.fileControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import main.java.application.WriteFileCmd;
import main.java.blockControl.BlockImpl;
import main.java.blockControl.BlockManagerImpl;
import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
import main.java.id.Id;
import main.java.util.FileUtil;

public class FileImpl implements File {
	String fileData;
	FileMeta fileMeta;
	long pointer = 0;
	Id fileId;
	
	public FileImpl(FileMeta fileMeta) {
		this.fileMeta = fileMeta;
		System.out.println("pointer when create new file object: " + pointer);
	}
	
	public FileImpl(Id fileId) {
		this.fileId = fileId;
	}
	
	@Override
	public Id getFileId() {
		return fileMeta.fileId;
	}

	@Override
	public FileManager getFileManager() {
		return new FileManagerImpl(fileMeta.getFmId());
	}

	@Override
	public byte[] read(int length) {
		System.out.println("用户读取操作的起始指针: " + pointer);
		if(length + pointer > size()) {
			throw new ErrorCode(9);
		}
		//文件的所有字节
		byte[] total = new byte[(int)size()];
		//用户想要读取的字节
		System.out.println("用户想要读取的字节数: " + length);
		byte[] result = new byte[length];
		
		HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = fileMeta.getLogicBlocks();
		for(Integer i : logicBlocks.keySet()) {
			LinkedList<BlockImpl> duplicates = logicBlocks.get(i);
			byte[] tempContent = null;
			while(duplicates.size() != 0) {
				BlockImpl block = duplicates.get(0);
				if(block.isValid()) {
					tempContent = FileUtil.reads(block.getPath() + FileConstant.DATA_SUFFIX);
					break;
				} else {
					duplicates.remove(0);
				}
			}
			
			//如果该logicBlock下的所有block都无效，那么为文件空洞
			if(duplicates.size() == 0) {
				tempContent = new byte[FileConstant.BLOCK_SIZE];
				throw new ErrorCode(2);
			}
			//拼接每个block的内容
			System.arraycopy(tempContent, 0, total, i * FileConstant.BLOCK_SIZE, tempContent.length);
		}
		System.arraycopy(total, (int) pointer, result, 0, length);
		
		move(length, 0);
		try {
			fileMeta.write();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
		int blockCount = fileMeta.blockCount;
		
		/**
		 * pointer在file的尾部或尾部之后
		 */
		if(pointer >= fileMeta.fileSize) {
			
			long fileHoleCount = pointer - fileSize;
			System.out.println("空洞: " + fileHoleCount);
			System.out.println("pointer在file的尾部或尾部之后");
			//情况一： 之前文件位占满block
			//需要读取之前block的内容
			if(fileSize % FileConstant.BLOCK_SIZE != 0) {
				System.out.println("需要读取之前block的内容！");
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
				subWrite(large, blockCount - 1, fileSize + large.length - fileSize % FileConstant.BLOCK_SIZE);
				
			//最后一个block被填满，可以直接写
			} else if(fileSize % FileConstant.BLOCK_SIZE == 0){ 
				System.out.println("最后一个block被填满，可以直接写！");
				byte[] large = new byte[(int) (fileHoleCount + bytes.length)];
				//添加文件空洞的内容
				for(int i = 0; i < fileHoleCount; i++) {
					large[i] = 0x00;
				}
				//复制要写入的内容到大数组的最后一部分
				System.arraycopy(bytes, 0, large, (int)fileHoleCount, bytes.length);
				subWrite(large, blockCount, fileSize + large.length);
			}
		
		/**
		 * pointer在file中部
		 */
		} else {
			System.out.println("pointer在file中部");
			int preBlockCount = (int) (pointer / FileConstant.BLOCK_SIZE);
			long newSize = 0;
			byte[] large;
			byte[] larger;
			
			//需要读取之前block的内容
			if(pointer % FileConstant.BLOCK_SIZE != 0) {
				System.out.println("需要读取之前block的内容");
				int leftInBlock = (int) (pointer % FileConstant.BLOCK_SIZE);
				BlockImpl blockBeforeWrite = logicBlocks.get(preBlockCount).get(0);
				byte[] temp = blockBeforeWrite.read();
				newSize = leftInBlock + bytes.length;
				large = new byte[(int) newSize];
				System.arraycopy(temp, 0, large, 0, leftInBlock);
				System.arraycopy(bytes, 0, large, leftInBlock, bytes.length);
				
			//可以直接写
			} else {
				System.out.println("可以直接写");
				newSize = bytes.length;
				large = new byte[bytes.length];
				System.arraycopy(bytes, 0, large, 0, bytes.length);
			}
			
			if((bytes.length < fileSize - pointer) && newSize % FileConstant.BLOCK_SIZE != 0) {
				System.out.println("未超出fileSize末尾需要补上");
				BlockImpl blockInTail = logicBlocks.get(preBlockCount + (int)(newSize / FileConstant.BLOCK_SIZE)).get(0);
				byte[] blockInTailContent = blockInTail.read();
				int addToTail = (int) (blockInTailContent.length - newSize % FileConstant.BLOCK_SIZE);
				larger = new byte[large.length + addToTail];
				System.arraycopy(large, 0, larger, 0, large.length);
				System.arraycopy(blockInTailContent, (int) (newSize % FileConstant.BLOCK_SIZE), larger, large.length, addToTail);
				
				subWrite(larger, preBlockCount, fileSize);
			} else if(bytes.length >= fileSize - pointer){
				
				System.out.println("超出fileSize");
				subWrite(large, preBlockCount, bytes.length + pointer);
			} else {
				System.out.println("未超出fileSize末尾不不不需要补上");
				subWrite(large, preBlockCount, fileSize);
			}
		}
		move(bytes.length, 0);
		System.out.println("after write the pointer is: " + pointer);
		
	}
	
	void subWrite(byte[] bytes, int beginBlock, long totalFileSize) {
		BlockManagerImpl bm;
		
		int newBlockCount = (bytes.length % FileConstant.BLOCK_SIZE) == 0
				? (bytes.length / FileConstant.BLOCK_SIZE)
				: (bytes.length / FileConstant.BLOCK_SIZE) + 1;
		System.out.println("new block count : " + newBlockCount);
		
		for(int i = 0; i < newBlockCount; i++) {
			LinkedList<BlockImpl> duplicates = new LinkedList<BlockImpl>();
		
			int begin = i * FileConstant.BLOCK_SIZE;
			int end = (i + 1) * FileConstant.BLOCK_SIZE;
			//System.out.println("copy begins at: " + begin + "\n ends at: " + end);
			
			//创建多个相同的副本
			for(int j = 0; j < FileConstant.DUPLICATION_COUNT; j++) {
				byte[] temp;
				if(end > bytes.length) {
					temp = Arrays.copyOfRange(bytes, begin, bytes.length);			
				} else {
					temp = Arrays.copyOfRange(bytes, begin, end);
				}		
				System.out.println("写入的字节: " + new String(temp));
				bm = WriteFileCmd.allocBm();
				System.out.println("in fileImpl creating block manager id: " + bm.getStringBmId());
				BlockImpl block = (BlockImpl) bm.newBlock(temp);
				
				duplicates.add(block);
			}
			
				fileMeta.addLogicBlocks(i + beginBlock, duplicates);
			
		}
		
		fileMeta.setFileSize(totalFileSize);
		
		fileMeta.setBlockCount((int) ((totalFileSize % FileConstant.BLOCK_SIZE) == 0
				? (totalFileSize / FileConstant.BLOCK_SIZE)
				: (totalFileSize / FileConstant.BLOCK_SIZE) + 1));
		
		//4. 写入完成后，将fileMeta的内容写回对应文件
		try {
			fileMeta.write();
		} catch (Exception e1) {		
			System.out.println(e1.getMessage());
		}
		
		try {
			
			fileMeta.read();
		} catch (Exception e) {		
			System.out.println(e.getMessage());
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
		return fileMeta.fileSize;
	}

	@Override
	public void setSize(long newSize) {
		byte[] oldContent = read((int) fileMeta.fileSize);
		byte[] newContent = new byte[(int) newSize];
		long oldSize = oldContent.length;
		int diff = (int) (newSize - oldSize);
		
		//原文件长度小于或等于新的长度，在后面加0x00
		if(diff >= 0) {
			System.arraycopy(oldContent, 0, newContent, 0, (int) oldSize);
			for(int i = 0; i < diff; i++) {
				newContent[(int) (oldSize + i)] = 0x00;
			}
		} else {
			System.arraycopy(oldContent, 0, newContent, 0, (int) newSize);
		}
		fileMeta.logicBlocks.clear();
		subWrite(newContent, 0, newSize);

	}
	public void setFileMeta(FileMeta fileMeta) {
		this.fileMeta = fileMeta;	
	}

}
