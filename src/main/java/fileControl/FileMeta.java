package main.java.fileControl;

import java.io.Serializable;
import java.util.LinkedList;

import main.java.blockControl.BlockImpl;
import main.java.constant.FileConstant;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class FileMeta implements Serializable {
	int fileSize;
	int blockSize;
	int blockCount;
	Id fileId;
	BlockImpl[][] logicBlocks = new BlockImpl[100][100];
	private static final long serialVersionUID = -5248069984631225347L;
	
	FileMeta(int fileSize) {
		this.fileSize = fileSize;
		blockCount = (fileSize % blockSize) == 0 ? fileSize / blockSize : fileSize / blockSize + 1;
		for(int i = 0; i < blockCount; i++) {
			for(int j = 0; j < fenpeisuanfa(); j++) {
				logicBlocks[i][j] = new BlockImpl(" "); //这几个block的内容相同
			}
			
		}
	}
	FileMeta(Id fileId) {
		this.fileId = fileId;
		this.fileSize = 0;
		this.blockSize = 0;
		this.blockCount = 0;
		logicBlocks[0][0] = new BlockImpl("blockk111");
		System.out.println(fileId.toString());
//		String destFilename = "fm" + FileConstant.PATH_SEPARATOR + fmId +
//				FileConstant.PATH_SEPARATOR + fileId.toString() + FileConstant.FILEMETA_SUFFIX;
//		FileUtil.createFile(destFilename);
	}
	
	public int write() throws Exception {
		
		byte[] bytes = SerializeUtil.toBytes(this);
		System.out.println("bytes: ....");
		for(int i = 0; i < bytes.length; i++) {
			System.out.println(bytes[i]);
		}
		FileMeta deserialize = SerializeUtil.deserialize(bytes, FileMeta.class);
		System.out.println(deserialize);
		return 0;
		//FileUtil.write();
	}
	
	int fenpeisuanfa() {
		return 1;
	}
	
	@Override
    public String toString() {
		String str = "FileMeta{" +
                "fileId = " + fileId +
                ", filesize = '" + fileSize + '\'' +
                ", blockSize = " + blockSize +
                ", blockCount = " + blockCount + 
                '}';
		for(int i = 0; i < logicBlocks[0].length; i++) {
			for(int j = 0; j < logicBlocks.length; j++) 
				if(logicBlocks[i][j] != null) {
					str += "\n" + logicBlocks[i][j].name;
				}
		}
        return str;
    }

}
