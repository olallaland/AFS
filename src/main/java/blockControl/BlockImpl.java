package main.java.blockControl;

import java.io.Serializable;

public class BlockImpl implements Block, Serializable {
	public String name;
	public BlockImpl(String name) {
		this.name = name;
	}
	@Override
	public Id getIndexId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockManager getBlockManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] read() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int blockSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
