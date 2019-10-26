package main.java.blockControl;

import main.java.id.Id;

interface BlockManager {
	Block getBlock(Id indexId);
	Block newBlock(byte[] b);
	default Block newEmptyBlock(int blockSize) {
		return newBlock(new byte[blockSize]);
	}
}
