package main.java.application;

import main.java.constant.FileConstant;
import main.java.fileControl.FileMeta;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class ReadBlockInHexCmd extends Command {
	public ReadBlockInHexCmd(String[] cmds) {
		//1. ����û�ָ������ĵ���ȷ�ԣ����������Լ��ļ��Ƿ���ڣ�
		if(cmds.length != 2) {
			throw new RuntimeException("Usage of alpha-hex: alpha-hex [blockId]"); 
		} else {
			try {
				readBlockInHex(cmds[1]);
			} catch(Exception e) {
				System.out.println(e);
			}
		}	
	}

	private void readBlockInHex(String blockId) {
		int blockIndex = 0;
		byte[] blockData;
		
		System.out.println(blockId);
		StringBuilder path = new StringBuilder();
		if(FileUtil.blockExists(blockId + FileConstant.DATA_SUFFIX, path)) {
			//��һ����Ҫ���ϼ���block�Ƿ���Ч
			blockData = FileUtil.reads(path.toString());
			for(int i = 0; i < blockData.length; i++) {
				System.out.print("0x" + Integer.toHexString(blockData[i] & 0xFF) + " ");
				if((i + 1) % 16 == 0) {
					System.out.println();
				}
			}
			System.out.println();
		} else {
			throw new RuntimeException("block not exists");
		}
		
	}
	
	static FileMeta findFile(String filename) {
		StringBuilder path = new StringBuilder();
		byte[] serializedContent;
		if(FileUtil.exists(filename + FileConstant.META_SUFFIX, path)) {
			//���filemeta�ļ�����
			serializedContent = FileUtil.reads(path.toString());
			System.out.println(path.toString());
			FileMeta fileMeta = SerializeUtil.deserialize(FileMeta.class, serializedContent);
			return fileMeta;
		} else {
			throw new RuntimeException(filename + ": file not exists");
		}
	}
}
