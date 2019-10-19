package main.java.blockControl;

import java.io.IOException;

import main.java.constant.FileConstant;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class BlockMeta {
	int blockSize = FileConstant.BLOCK_SIZE;
	long checkSum;
	
	BlockMeta(long checkSum) {
		this.checkSum = checkSum;
	}
	
	int write(String path) throws Exception {
		byte[] bytes = SerializeUtil.toBytes(this);
		FileUtil.writes(bytes, path);
		//System.out.println(SerializeUtil.toBytes(this, path));
		return 0;
	}
}
