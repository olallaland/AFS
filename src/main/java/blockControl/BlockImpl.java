package main.java.blockControl;

import java.io.IOException;
import java.io.Serializable;

import main.java.constant.FileConstant;
import main.java.util.FileUtil;

public class BlockImpl implements Block, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3560344460655349302L;
	public String name;
	Id blockId;
	BlockManagerImpl bm;
	BlockMeta blockMeta;
	byte[] blockData;
	String path;
	
	public BlockImpl(String name) {
		this.name = name;
		StringBuilder tempPath = new StringBuilder();
//		FileUtil.exists(name + FileConstant.DATA_SUFFIX, tempPath);
//		System.out.println(tempPath);
//		this.path = tempPath.toString().substring(0, tempPath.length() - 5);
	}
	public BlockImpl(BlockId id, BlockManagerImpl bm, byte[] blockData) {
		this.blockId = id;
		this.bm = bm;
		this.path = FileConstant.BM_CWD + FileConstant.PATH_SEPARATOR + bm.bmId +
				FileConstant.PATH_SEPARATOR + blockId.toString();
		this.blockData = blockData;
		this.blockMeta = new BlockMeta(checkSum(blockData));
	}
	@Override
	public Id getIndexId() {
		// TODO Auto-generated metod stub
		return blockId;
	}

	@Override
	public BlockManager getBlockManager() {
		// TODO Auto-generated method stub
		return bm;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public boolean isValid() {
		String dataPath = this.path + FileConstant.DATA_SUFFIX;
		byte[] tempContent = FileUtil.reads(dataPath);
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
	//blockData和blockMeta文件
	int writeBlockData() {
		try {
			FileUtil.writes(blockData, path + FileConstant.DATA_SUFFIX);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("写BlockData时发生错误：" + e);
		}
		return 0;
	}
	
	int writeBlockMeta() {
		try {
			blockMeta.write(path + FileConstant.META_SUFFIX);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("写BlockMeta时发生错误：" + e);
		}
		return 0;
	}
	
	long checkSum(byte[] content) {
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
