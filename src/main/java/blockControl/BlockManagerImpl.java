package main.java.blockControl;

import java.io.IOException;
import java.util.HashMap;

import main.java.util.FileUtil;

public class BlockManagerImpl implements BlockManager {
	String bmId;
	HashMap<Id, BlockImpl> blockSet;
	
	public BlockManagerImpl(String bmId) {
		this.bmId = bmId;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Block getBlockId(Id indexId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block newBlock(byte[] b) {
		int count = 0;
		BlockImpl block;
		try {
			count = FileUtil.readIdCount();
			block = new BlockImpl(new BlockId(count), this, b);
			block.write();
			blockSet.put(new BlockId(count), block);
			
			count++;
			FileUtil.updateIdCount(count);			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}
	
	public int addBlock(BlockImpl b) {
		blockSet.put(b.getIndexId(), b);
		return 0;
	}

}
