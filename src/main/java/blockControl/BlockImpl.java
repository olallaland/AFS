package main.java.blockControl;

import java.io.IOException;
import java.io.Serializable;

import main.java.constant.FileConstant;
import main.java.id.IntegerId;
import main.java.id.StringId;
import main.java.id.Id;
import main.java.util.FileUtil;

public class BlockImpl implements Block, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3560344460655349302L;
	public String name;
	Id blockId;
	Id bmId;
	BlockMeta blockMeta;
	byte[] blockData;
	String path;
	
	public BlockImpl() {

	}
	public BlockImpl(IntegerId id, Id bmId, byte[] blockData) {
		this.blockId = id;
		this.bmId = bmId;
		this.path = FileConstant.BM_CWD + FileConstant.PATH_SEPARATOR + getStringBmId() +
				FileConstant.PATH_SEPARATOR + getIntegerBlockId();
		this.blockData = blockData;
		this.blockMeta = new BlockMeta(checkSum(blockData));
	}
	@Override
	public Id getIndexId() {
		return blockId;
	}
	
	public Integer getIntegerBlockId() {
		if(blockId instanceof IntegerId) {
			IntegerId sid = (IntegerId) blockId;
			int id = sid.getId();
			return id;
		} else {
			//TODO
			return 0;
		}
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
	
	public String getPath() {
		return this.path;
	}
	
	public boolean isValid() {
		String dataPath = this.path + FileConstant.DATA_SUFFIX;
		byte[] tempContent = null;
		try {
			tempContent = FileUtil.reads(dataPath);
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
		}
		if(blockMeta.getCheckSum() == checkSum(tempContent)) {
			System.out.println(blockMeta.getCheckSum());
			System.out.println(checkSum(tempContent));
			System.out.println("block " + getIntegerBlockId() + " is valid");
		} else {
			System.out.println("block " + getIntegerBlockId() + " is invalid");
		}
		return blockMeta.getCheckSum() == checkSum(tempContent);
	}

	@Override
	public byte[] read() {
		// TODO Auto-generated method stub
		return FileUtil.reads(this.path + FileConstant.DATA_SUFFIX);
	}

	@Override
	public int blockSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	int write() {
		writeBlockData();
		writeBlockMeta();
		return 0;
	}
	//blockDataºÍblockMetaÎÄ¼þ
	int writeBlockData() {
		try {
			FileUtil.writes(blockData, path + FileConstant.DATA_SUFFIX);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}
	
	int writeBlockMeta() {
		try {
			blockMeta.write(path + FileConstant.META_SUFFIX);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}
	
	long checkSum(byte[] content) {
		long number = content.length;
		long temp = Long.parseLong("1821349192381");
		for(byte b : content) {
			long lb = (long)b;
			number = ((number << 1) | (number >>> 63) |
					 ((lb & 0xc3) << 41) | ((lb & 0xa7) << 12)) + 
					 lb * 91871341 + temp;
		}
		return number;
	}
	@Override
	public BlockManager getBlockManager() {
		return new BlockManagerImpl(bmId);
	}

}
