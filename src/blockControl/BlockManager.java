package blockControl;

import fileControl.Id;

interface BlockManager {
	Block getBlockId(Id indexId);
	Block newBlock(byte[] b);
	default Block newEmptyBlock(int blockSize) {
		return newBlock(new byte[blockSize]);
	}
}
