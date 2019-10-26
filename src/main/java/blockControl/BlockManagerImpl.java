package main.java.blockControl;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import main.java.id.Id;
import main.java.id.IntegerId;
import main.java.id.StringId;
import main.java.util.FileUtil;

public class BlockManagerImpl implements BlockManager, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6437111830025101381L;
	Id bmId;
	HashMap<Id, BlockImpl> blockSet = new HashMap<Id, BlockImpl>();
	
	public BlockManagerImpl(Id bmId) {
		this.bmId = bmId;	
	}

	public BlockManagerImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Block getBlock(Id indexId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getStringBmId() {
		if(bmId instanceof StringId) {
			StringId sid = (StringId) bmId;
			String id = sid.getId();
			return id;
		} else {
			//TODO
			return "";
		}
	}

	@Override
	public Block newBlock(byte[] b) {
		int count = 0;
		BlockImpl block = new BlockImpl();
		try {
			count = FileUtil.readIdCount();
			block = new BlockImpl(new IntegerId(count), bmId, b);
			block.write();
			blockSet.put(new IntegerId(count), block);
			
			count++;
			FileUtil.updateIdCount(count);			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("read countÊ±³ö´í: " + e1);
		}
		
		return block;
	}
	
	public int addBlock(BlockImpl b) {
		blockSet.put(b.getIndexId(), b);
		return 0;
	}

}
