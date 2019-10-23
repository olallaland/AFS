package main.java.application;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import main.java.blockControl.BlockImpl;
import main.java.blockControl.BlockManagerImpl;
import main.java.constant.FileConstant;
import main.java.fileControl.FileID;
import main.java.fileControl.FileImpl;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;
import main.java.util.BinaryUtil;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class WriteFileCmd extends Command {
	
	public WriteFileCmd(String[] cmds) throws UnsupportedEncodingException {
		//1. 检查用户指令输入的的正确性
		if(cmds.length < 5) {
			throw new RuntimeException("Usage of alpha-wirte: alpha-write [-b filename] "
					+ "[-n where] [-n offset] [\"content\"]");
		} else {
			//拼接被空格号隔开的内容
			writeFile(cmds);
		}
	}
	
	public static int writeFile(String[] cmds) throws UnsupportedEncodingException {
		int where = 0;
		long offset = 0;	
		String filename = "";
		String content = "";
		int blockCount = 0;
		HashMap<Integer, BlockImpl> duplicates;
		BlockManagerImpl bm;
	
		try {
			where = Integer.valueOf(cmds[2]);//获得指针移动的初始位置
			offset = Long.valueOf(cmds[3]);//获得指针移动的偏移量
		} catch(Exception e ) {
			System.out.println("转化数字" + e);
		}
		
		//拼接用空格隔开的content
		for(int i = 4; i < cmds.length; i++) {
			if(i == cmds.length - 1) {
				content += cmds[i];
			} else {
				content += cmds[i] + " ";
			}
		}
		//去掉首尾的双引号
		content = content.substring(1, content.length() - 1);
		System.out.println(content);
	
		//2. 若是用户指令输入无误，根据用户输入的filename查找到对应的fileMeta文件
		//并将其内容反序列化，生成对应的fileMeta对象
		filename = cmds[1];
		FileMeta fileMeta = findFile(filename);
		System.out.println(fileMeta);
		FileImpl file = new FileImpl(fileMeta);
		file.move(offset, where);
		file.write(content.getBytes("utf-8"));
		
		
//		//3. 计算需要分配的block数量
//		//每个block由随机分配的BlockManager创建，并将内容写入
//		//对应修改fileMeta的logicBlock部分
//		byte[] bytes = content.getBytes("utf-8");
//		file.write(bytes);
//		
//		blockCount = (bytes.length % FileConstant.BLOCK_SIZE) == 0
//				? (bytes.length / FileConstant.BLOCK_SIZE)
//				: (bytes.length / FileConstant.BLOCK_SIZE) + 1;
//		
//		fileMeta.setFileSize(bytes.length);
//		fileMeta.setBlockCount(blockCount);
//		
//		for(int i = 0; i < blockCount; i++) {
//			LinkedList<BlockImpl> logicBlocks = new LinkedList<BlockImpl>();
//			byte[] temp;
//			int begin = i * FileConstant.BLOCK_SIZE;
//			int end = (i + 1) * FileConstant.BLOCK_SIZE;
//			System.out.println("copy begins at: " + begin + "\n ends at: " + end);
//			if(end > bytes.length) {
//				temp = Arrays.copyOfRange(bytes, begin, bytes.length);			
//			} else {
//				temp = Arrays.copyOfRange(bytes, begin, end);
//			}		
//			System.out.println("写入的字节: " + new String(temp));
//			bm = allocBm();
//			BlockImpl block = (BlockImpl) bm.newBlock(temp);
//			try {
//			logicBlocks.add(block);
//			//修改fileMeta的logicBlock块
//			fileMeta.setLogicBlocks(i, logicBlocks);
//			} catch(Exception e) {
//				System.out.println("Add block to fileMeta error : " + e);
//			}
//		}
//		
//		//4. 写入完成后，将fileMeta的内容写回对应文件
//		try {
//			fileMeta.write();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		try {
//			
//			fileMeta.read();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			System.out.println("读取fileMeta时发生错误：" + e);
//		}
		
		return 0;
	}
	
	//为新开的块分配BlockManager
	public static BlockManagerImpl allocBm() {
		int bmIndex = (int) (Math.random() * FileConstant.BM_COUNT + 1);
		String bmId = "bm-" + bmIndex;
		BlockManagerImpl bm = new BlockManagerImpl();
		
		//查询分配的bm对象是否已创建
		try {
			if(currBlockManagers.containsKey(bmId)) {
				bm = currBlockManagers.get(bmId);
			} else {
				bm = new BlockManagerImpl(bmId);
				currBlockManagers.put(bmId, bm);
			}
		} catch(Exception e) {
			System.out.println("allocate bm error : " + e);
		}
		return bm;
	}
	
	static FileMeta findFile(String filename) {
		//先从fm的集合里查找
		StringBuilder path = new StringBuilder();
		byte[] serializedContent;
		if(FileUtil.exists(filename + FileConstant.META_SUFFIX, path)) {
			//获得filemeta文件内容
			serializedContent = FileUtil.reads(path.toString());
			System.out.println(path.toString());
			FileMeta fileMeta = SerializeUtil.deserialize(FileMeta.class, serializedContent);
			return fileMeta;
		} else {
			throw new RuntimeException(filename + ": file not exists");
		}
	}

}
