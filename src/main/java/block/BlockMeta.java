package main.java.block;

import java.io.Serializable;

import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class BlockMeta implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1936056658480287561L;
	int blockSize = FileConstant.BLOCK_SIZE;
	long checkSum;
	
	BlockMeta(long checkSum) {
		this.checkSum = checkSum;
	}
	
	public long getCheckSum() {
		return this.checkSum;
	}
	
	int write(String path) throws Exception {
		byte[] bytes = SerializeUtil.toBytes(this);
		try {
			FileUtil.writes(bytes, path);
		} catch(RuntimeException e) {
			throw new ErrorCode(1);
		}

		return 0;
	}
}
