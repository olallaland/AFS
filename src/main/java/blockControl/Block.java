package main.java.blockControl;

import main.java.id.Id;

interface Block {
	Id getIndexId();
	BlockManager getBlockManager();
	byte[] read();
	int blockSize();
}
