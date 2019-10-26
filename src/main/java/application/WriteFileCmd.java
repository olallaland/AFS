package main.java.application;

import java.io.UnsupportedEncodingException;

import main.java.exception.ErrorCode;
import main.java.fileControl.FileImpl;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;

public class WriteFileCmd extends Command {
	
	public WriteFileCmd(String[] cmds) throws UnsupportedEncodingException {
		//1. ����û�ָ������ĵ���ȷ��
		if(cmds.length < 5) {
			throw new ErrorCode(7);
		} else {
			//ƴ�ӱ��ո�Ÿ���������
			writeFile(cmds);
		}
	}
	
	public static int writeFile(String[] cmds) throws UnsupportedEncodingException {
		int where = 0;
		long offset = 0;	
		String filename = "";
		String content = "";
		FileMeta fileMeta = null;
	
		try {
			where = Integer.valueOf(cmds[2]);//���ָ���ƶ��ĳ�ʼλ��
			offset = Long.valueOf(cmds[3]);//���ָ���ƶ���ƫ����
		} catch(Exception e ) {
			throw new ErrorCode(8);
		}
		
		//ƴ���ÿո������content
		for(int i = 4; i < cmds.length; i++) {
			if(i == cmds.length - 1) {
				content += cmds[i];
			} else {
				content += cmds[i] + " ";
			}
		}
		//ȥ����β��˫����
		content = content.substring(1, content.length() - 1);
		System.out.println(content);
	
		//2. �����û�ָ���������󣬸����û������filename���ҵ���Ӧ��fileMeta�ļ�
		//���������ݷ����л������ɶ�Ӧ��fileMeta����
		filename = cmds[1];
		//��һ���Ƶ�fileManager����
		try {
			fileMeta = findFile(filename);
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(fileMeta);
		FileManagerImpl fm = getFmById(fileMeta.getFmId());
		FileImpl file = (FileImpl) fm.getFile(fileMeta.getFileId());
		file.setFileMeta(fileMeta);
		file.move(offset, where);
		
		try {
			file.write(content.getBytes("utf-8"));
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
		}
		
		return 0;
	}
}
