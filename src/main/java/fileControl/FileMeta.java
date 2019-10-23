package main.java.fileControl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import main.java.blockControl.BlockImpl;
import main.java.constant.FileConstant;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class FileMeta implements Serializable {
	int fileSize;
	int blockSize = FileConstant.BLOCK_SIZE;
	int blockCount = 0;
	String fmId;
	String path;
	Id fileId;
	HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = new HashMap<Integer, LinkedList<BlockImpl>>();
	//BlockImpl[][] logicBlocks = new BlockImpl[100][100];//可以改用数组+链表
	private static final long serialVersionUID = -5248069984631225347L;
	
//	FileMeta(int fileSize) {
//		this.fileSize = fileSize;//		blockCount = (fileSize % blockSize) == 0 ? fileSize / blockSize : fileSize / blockSize + 1;
//		for(int i = 0; i < blockCount; i++) {
//			for(int j = 0; j < fenpeisuanfa(); j++) {
//				//logicBlocks[i][j] = new BlockImpl(" "); //这几个block的内容相同
//			}
//			
//		}
//	}
	FileMeta(Id fileId, String fmId) {
		this.fileId = fileId;
		this.fmId = fmId;
		this.fileSize = 0;
		this.blockCount = (fileSize % blockSize) == 0
				? (fileSize / blockSize) : (fileSize / blockSize + 1);
		this.path = FileConstant.FM_CWD + FileConstant.PATH_SEPARATOR + fmId +
				FileConstant.PATH_SEPARATOR + fileId.toString() + FileConstant.META_SUFFIX;
		LinkedList<BlockImpl> blocks = new LinkedList<BlockImpl>();
		logicBlocks.put(0, blocks);
		FileUtil.createFile(path);
		try {
			write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		read();
	}
	
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	
	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public int getFileSize() {
		return fileSize;
	}
	
	public int write() throws Exception {
		byte[] bytes = SerializeUtil.toBytes(this);
		FileUtil.writes(bytes, path);
		//System.out.println(SerializeUtil.toBytes(this, path));
		return 0;
		//FileUtil.write();
	}
	
	public void read() {
	
		FileMeta deserialize = SerializeUtil.deserialize(FileMeta.class,FileUtil.reads(path));
		System.out.println(deserialize);
	}
	
	int fenpeisuanfa() {
		return 1;
	}
	
	@Override
    public String toString() {
		String str = "FileMeta{" +
                "fileId = " + fileId +
                ", filesize = " + fileSize +
                ", blockSize = " + blockSize +
                ", blockCount = " + blockCount + 
                ", path = " + path +
                '}';
		for(Integer i : logicBlocks.keySet()) {
			LinkedList<BlockImpl> block = logicBlocks.get(i);
			str += "\n" + i;
			for(int j = 0; j < block.size(); j++) {
				str += ": [" + (block.get(j).getIndexId()) + ", " + block.get(j).getBlockManager() + "]" ;
			}
		}

        return str;
    }
	
	public int setLogicBlocks(int index, LinkedList<BlockImpl> list) {
		
		System.out.println(index + ", " + list.get(0).getIndexId());
		logicBlocks.put(index, list);
		
		return 0;
	}

	public HashMap<Integer, LinkedList<BlockImpl>> getLogicBlocks() {
		// TODO Auto-generated method stub
		return this.logicBlocks;
	}

}
