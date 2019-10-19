package main.java.application;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;

import main.java.blockControl.BlockImpl;
import main.java.blockControl.BlockManagerImpl;
import main.java.constant.FileConstant;
import main.java.fileControl.FileID;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;
import main.java.util.BinaryUtil;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class WriteFileCmd extends Command {
	BlockManagerImpl bm;
	public WriteFileCmd(String[] cmds) throws UnsupportedEncodingException {
		int position = 0;
		String filename = "";
		String content = "";
		int blockCount = 0;
		HashMap<Integer, BlockImpl> duplicates;
	
		if(cmds.length < 4) {
			throw new RuntimeException("Usage of alpha-wirte: alpha-write [-n pointer] [-b filename] [\"content\"]");
		} else {
			//ƴ�ӱ��ո�Ÿ���������
			for(int i = 3; i < cmds.length; i++) {
				if(i == cmds.length - 1) {
					content += cmds[i];
				} else {
					content += cmds[i] + " ";
				}
			}
		}
		//ȥ����β��˫����
		content = content.substring(1, content.length() - 1);
		System.out.println(content);
		try {
			position = Integer.parseInt(cmds[1]);
		} catch(RuntimeException e) {
			System.out.println(e);
		}
		
		filename = cmds[2];
		FileMeta fileMeta = findFile(filename);
		
		//������Ҫ�����block����
		byte[] bytes = content.getBytes("utf-8");
		
		blockCount = (bytes.length % FileConstant.BLOCK_SIZE) == 0
				? (bytes.length % FileConstant.BLOCK_SIZE)
				: (bytes.length % FileConstant.BLOCK_SIZE) + 1;
		
		for(int i = 0; i < blockCount; i++) {
			byte[] temp;
			int begin = i * FileConstant.BLOCK_SIZE;
			if((i + 1) * FileConstant.BLOCK_SIZE > bytes.length) {
				temp = Arrays.copyOfRange(bytes, begin, bytes.length - begin);
			} else {
				temp = Arrays.copyOfRange(bytes, begin, FileConstant.BLOCK_SIZE);
			}		
			bm = allocBm();
			bm.newBlock(temp);
		}
	}
	
	//Ϊ�¿��Ŀ����BlockManager
	static BlockManagerImpl allocBm() {
		String bmId = "bm-01";
		BlockManagerImpl bm;
		
		//��ѯ�����bm�����Ƿ��Ѵ���
		if(currBlockManagers.get(bmId) != null) {
			bm = currBlockManagers.get(bmId);
		} else {
			bm = new BlockManagerImpl(bmId);
			currBlockManagers.put(bmId, bm);
		}
		return bm;
	}
	
	static FileMeta findFile(String filename) {
		StringBuilder path = new StringBuilder();
		byte[] serializedContent;
		if(FileUtil.exists(filename, path)) {
			//���filemeta�ļ�����
			serializedContent = FileUtil.reads(path.toString());
			FileMeta fileMeta = SerializeUtil.deserialize(FileMeta.class, serializedContent);
			return fileMeta;
		} else {
			throw new RuntimeException(filename + ": file not exists");
		}
	}

}
