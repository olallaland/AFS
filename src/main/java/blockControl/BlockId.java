package main.java.blockControl;

import java.io.Serializable;

public class BlockId implements Id, Serializable {
	int index;
	public BlockId(int index) {
		this.index = index;
	}
	@Override
	public String toString() {
		return index + "";
	}
}
