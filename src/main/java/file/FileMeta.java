package main.java.file;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import main.java.block.BlockImpl;
import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
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
		} catch (ErrorCode e) {
			throw new ErrorCode(e.getErrorCode());
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

	/**
	 * get the string fmId
	 * @return
	 */
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

	/**
	 * get the string fileId
	 * @return
	 */
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

	/**
	 * 将fileMeta序列化并写入文件
	 * @return
	 */
	public int write() {
		this.path = setPath(fmId, fileId);
		byte[] bytes;
		try {
			bytes = SerializeUtil.toBytes(this);
		} catch (Exception e) {
			throw new ErrorCode(14);
		}
		try {
			FileUtil.writes(bytes, path);
		} catch (IOException e) {
			throw new ErrorCode(1);
		}
		//System.out.println(SerializeUtil.toBytes(this, path));
		return 0;
		//FileUtil.write();
	}

	/**
	 * 反序列化并生成FileMeta对象
	 */
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

		logicBlocks.put(index, list);
		
		return 0;
	}

}
