package main.java.fileControl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import main.java.blockControl.BlockImpl;
import main.java.constant.FileConstant;
import main.java.id.Id;
import main.java.id.StringId;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class FileMeta implements Serializable {
	long fileSize;
	int blockSize = FileConstant.BLOCK_SIZE;
	int blockCount = 0;
	Id fmId;
	String path;
	Id fileId;
	HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = new HashMap<Integer, LinkedList<BlockImpl>>();
	//BlockImpl[][] logicBlocks = new BlockImpl[100][100];//可以改用数组+链表
	private static final long serialVersionUID = -5248069984631225347L;
	
	public FileMeta() {
	}
	
	FileMeta(Id fileId, Id fmId) {
		this.fileId = fileId;
		this.fmId = fmId;
		this.fileSize = 0;
		this.blockCount = (int) ((fileSize % blockSize) == 0
				? (fileSize / blockSize) : (fileSize / blockSize + 1));
		this.path = setPath(fmId, fileId);
		FileUtil.createFile(path);
		try {
			write();
		} catch (Exception e) {
			System.out.println(e.getMessage());
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

	public String setPath(Id fmId, Id fileId) {
		String newPath = "";
		
		newPath = FileConstant.FM_CWD + FileConstant.PATH_SEPARATOR + getStringFmId() +
				FileConstant.PATH_SEPARATOR + getStringFileId() + FileConstant.META_SUFFIX;
				
		return newPath;
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
	public Id getFmId() {
		return this.fmId;
	}
	
	public String getStringFmId() {
		if(fmId instanceof StringId) {
			StringId sid = (StringId) fmId;
			String id = sid.getId();
			return id;
		} else {
			//TODO
			return "";
		}
	}

	/**
	 * @param fmId the fmId to set
	 */
	public void setFmId(Id fmId) {
		this.fmId = fmId;
	}

	public Id getFileId() {
		return this.fileId;
	}
	
	public String getStringFileId() {
		if(fileId instanceof StringId) {
			StringId sid = (StringId) fileId;
			String id = sid.getId();
			return id;
		} else {
			//TODO
			return "";
		}
	}

	public void setFileId(Id fileId) {
		this.fileId = fileId;
	}
	 
	
	public int write() throws Exception {
		this.path = setPath(fmId, fileId);
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
                "fileId = " + getStringFileId() +
                ", filesize = " + fileSize +
                ", blockSize = " + blockSize +
                ", blockCount = " + blockCount + 
                ", path = " + path +
                '}';
		for(Integer i : logicBlocks.keySet()) {
			LinkedList<BlockImpl> block = logicBlocks.get(i);
			str += "\n" + i;
			for(int j = 0; j < block.size(); j++) {
				str += ": [" + (block.get(j).getIntegerBlockId()) + ", " + block.get(j).getStringBmId() + "]" ;
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
