package blockControl;

import fileControl.Id;

interface Block {
	Id getIndexId();
	BlockManager getBlockManager();
	byte[] read();
	int blockSize();
}
