package main.java.fileControl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import main.java.blockControl.BlockImpl;
import main.java.constant.FileConstant;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class FileMeta implements Serializable {
	int fileSize;
	int blockSize = FileConstant.BLOCK_SIZE;
	int blockCount = 0;
	String fmId;
	String path;
	Id fileId;
	HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = new HashMap<Integer, LinkedList<BlockImpl>>();
	//BlockImpl[][] logicBlocks = new BlockImpl[100][100];//���Ը�������+����
	private static final long serialVersionUID = -5248069984631225347L;
	
	FileMeta(int fileSize) {
		this.fileSize = fileSize;
		blockCount = (fileSize % blockSize) == 0 ? fileSize / blockSize : fileSize / blockSize + 1;
		for(int i = 0; i < blockCount; i++) {
			for(int j = 0; j < fenpeisuanfa(); j++) {
				//logicBlocks[i][j] = new BlockImpl(" "); //�⼸��block��������ͬ
			}
			
		}
	}
	FileMeta(Id fileId, String fmId) {
		this.fileId = fileId;
		this.fmId = fmId;
		this.fileSize = 0;
		this.blockCount = (fileSize % blockSize) == 0
				? (fileSize / blockSize) : (fileSize / blockSize + 1);
		this.path = FileConstant.FM_CWD + FileConstant.PATH_SEPARATOR + fmId +
				FileConstant.PATH_SEPARATOR + fileId.toString() + FileConstant.META_SUFFIX;
		LinkedList<BlockImpl> blocks = new LinkedList<BlockImpl>();
		blocks.add(new BlockImpl("0"));
		blocks.add(new BlockImpl("00"));
		logicBlocks.put(0, blocks);
		FileUtil.createFile(path);
		try {
			write(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		read();
	}
	
	public int write(FileData data) throws Exception {
		byte[] bytes = SerializeUtil.toBytes(this);
		FileUtil.writes(bytes, path);
		//System.out.println(SerializeUtil.toBytes(this, path));
		return 0;
		//FileUtil.write();
	}
	
	public void read() {
	
		FileMeta deserialize = SerializeUtil.deserialize(FileMeta.class,FileUtil.reads(path));
		System.out.println(deserialize);
	}
	
	int fenpeisuanfa() {
		return 1;
	}
	
	@Override
    public String toString() {
		String str = "FileMeta{" +
                "fileId = " + fileId +
                ", filesize = '" + fileSize + '\'' +
                ", blockSize = " + blockSize +
                ", blockCount = " + blockCount + 
                ", path = " + path +
                '}';
		for(Integer i : logicBlocks.keySet()) {
			LinkedList<BlockImpl> block = logicBlocks.get(i);
			for(int j = 0; j < block.size(); j++) {
				str += "\n" + (block.get(j).name);
			}
		}

        return str;
    }

}
