package main.java.application;

import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
import main.java.util.FileUtil;

public class ReadBlockInHexCmd extends Command {
	public ReadBlockInHexCmd(String[] cmds) {
		//1. ����û�ָ������ĵ���ȷ�ԣ����������Լ��ļ��Ƿ���ڣ�
		if(cmds.length != 2) {
			throw new ErrorCode(7);
		} else {
			try {
				readBlockInHex(cmds[1]);
			} catch(Exception e) {
				System.out.println(e);
			}
		}	
	}

	private void readBlockInHex(String blockId) {
		byte[] blockData = null;
		
		System.out.println(blockId);
		StringBuilder path = new StringBuilder();
		if(FileUtil.blockExists(blockId + FileConstant.DATA_SUFFIX, path)) {
			//��һ����Ҫ���ϼ���block�Ƿ���Ч
			try {
				blockData = FileUtil.reads(path.toString());
			} catch(RuntimeException e) {
				System.out.println(e.getMessage());
			}
			for(int i = 0; i < blockData.length; i++) {
				System.out.print("0x" + Integer.toHexString(blockData[i] & 0xFF) + " ");
				if((i + 1) % 16 == 0) {
					System.out.println();
				}
			}
			System.out.println();
		} else {
			throw new ErrorCode(5);
		}
		
	}

}
 