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
	long fileSize;
	int blockSize = FileConstant.BLOCK_SIZE;
	int blockCount = 0;
	private String fmId;
	String path;
	Id fileId;
	HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = new HashMap<Integer, LinkedList<BlockImpl>>();
	//BlockImpl[][] logicBlocks = new BlockImpl[100][100];//可以改用数组+链表
	private static final long serialVersionUID = -5248069984631225347L;
	
	public FileMeta() {
	}
	
	FileMeta(Id fileId, String fmId) {
		this.fileId = fileId;
		this.setFmId(fmId);
		this.fileSize = 0;
		this.blockCount = (int) ((fileSize % blockSize) == 0
				? (fileSize / blockSize) : (fileSize / blockSize + 1));
		FileUtil.createFile(path);
		try {
			write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		read();
	}
	
	public void setFileSize(long totalFileSize) {
		this.fileSize = totalFileSize;
	}
	
	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public long getFileSize() {
		return this.fileSize;
	}
	
	public int getBlockCount() {
		return this.blockCount;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setLogicBlocks(HashMap<Integer, LinkedList<BlockImpl>> logicBlocks) {
		this.logicBlocks = logicBlocks;
	}
	
	public HashMap<Integer, LinkedList<BlockImpl>> getLogicBlocks() {
		// TODO Auto-generated method stub
		return this.logicBlocks;
	}

	/**
	 * @return the fmId
	 */
	public String getFmId() {
		return this.fmId;
	}

	/**
	 * @param fmId the fmId to set
	 */
	public void setFmId(String fmId) {
		this.fmId = fmId;
	}

	public Id getFileId() {
		// TODO Auto-generated method stub
		return this.fileId;
	}

	public void setFileId(Id fileId) {
		this.fileId = fileId;
	}
	 
	
	public int write() throws Exception {
		setPath(FileConstant.FM_CWD + FileConstant.PATH_SEPARATOR + fmId +
				FileConstant.PATH_SEPARATOR + fileId.toString() + FileConstant.META_SUFFIX);
		byte[] bytes = SerializeUtil.toBytes(this);
		FileUtil.writes(bytes, path);
		//System.out.println(SerializeUtil.toBytes(this, path));
		return 0;
		//FileUtil.write();
	}
	
	public void read() {	
		FileMeta deserialize = SerializeUtil.deserialize(FileMeta.class, FileUtil.reads(path));
		System.out.println(deserialize);
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
	
	public int addLogicBlocks(int index, LinkedList<BlockImpl> list) {
		
		System.out.println(index + ", " + list.get(0).getIndexId());
		logicBlocks.put(index, list);
		
		return 0;
	}

}
