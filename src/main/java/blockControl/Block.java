package main.java.blockControl;

import main.java.fileControl.Id;

interface Block {
	Id getIndexId();
	BlockManager getBlockManager();
	byte[] read();
	int blockSize();
}
