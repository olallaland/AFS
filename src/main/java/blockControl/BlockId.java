package main.java.blockControl;

public class BlockId implements Id {
	int index;
	public BlockId(int index) {
		this.index = index;
	}
	@Override
	public String toString() {
		return index + "";
	}
}
