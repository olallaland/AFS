package main.java.blockControl;

import java.io.IOException;
import java.io.Serializable;

import main.java.constant.FileConstant;
import main.java.util.FileUtil;

public class BlockImpl implements Block, Serializable {
	public String name;
	Id blockId;
	BlockManagerImpl bm;
	BlockMeta blockMeta;
	byte[] blockData;
	String path;
	
	public BlockImpl(String name) {
		this.name = name;
	}
	public BlockImpl(BlockId id, BlockManagerImpl bm, byte[] blockData) {
		this.blockId = id;
		this.bm = bm;
		this.path = FileConstant.BM_CWD + FileConstant.PATH_SEPARATOR + bm.bmId +
				FileConstant.PATH_SEPARATOR + blockId.toString();
		this.blockData = blockData;
		this.blockMeta = new BlockMeta(getCheckSum());
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	int writeBlockMeta() {
		try {
			blockMeta.write(path + FileConstant.META_SUFFIX);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	long getCheckSum() {
		long number = blockData.length;
		long temp = Long.parseLong("1821349192381");
		for(byte b : blockData) {
			long lb = (long)b;
			number = ((number << 1) | (number >>> 63) |
					 ((lb & 0xc3) << 41) | ((lb & 0xa7) << 12)) + 
					 lb * 91871341 + temp;
		}
		return number;
	}

}
