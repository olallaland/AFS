package fileControl;

import java.util.LinkedList;

public class FileMeta {
	int fileSize;
	int blockSize;
	int blockCount;
	Block[][] logicBlocks;
	
	FileMeta(int fileSize) {
		this.fileSize = fileSize;
		blockCount = (fileSize % blockSize) == 0 ? fileSize / blockSize : fileSize / blockSize + 1;
		for(int i = 0; i < blockCount; i++) {
			for(int j = 0; j < fenpeijizhi(); j++) {
				logicBlocks[i][j] = new Block(); //这几个block的内容相同
			}
			
		}
	}

}
