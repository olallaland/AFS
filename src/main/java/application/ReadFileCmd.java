package main.java.application;

import java.io.UnsupportedEncodingException;

import main.java.exception.ErrorCode;
import main.java.fileControl.FileImpl;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;

public class ReadFileCmd extends Command {
	public ReadFileCmd(String[] cmds) {
		//1. ����û�ָ������ĵ���ȷ�ԣ����������Լ��ļ��Ƿ���ڣ�
		if(cmds.length != 4) {
			throw new ErrorCode(7);
		} else {
			try {
				readFileContent(cmds);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}	
	}

	private void readFileContent(String[] cmds) {
		int where = 0;
		int length = 0;
		String filename;
		byte[] byteContent;
		String fileData = "";
		FileMeta fileMeta = null;
		
		try {
			where = Integer.valueOf(cmds[2]);//��ö�ȡ����ʼλ��
			length = Integer.valueOf(cmds[3]);//���Ҫ��ȡ�ĳ���
		} catch(Exception e ) {
			throw new ErrorCode(8);
		}
		
		filename = cmds[1];
		try {
			fileMeta = findFile(filename);
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(fileMeta);
		FileManagerImpl fm = getFmById(fileMeta.getFmId());
		FileImpl file = (FileImpl) fm.getFile(fileMeta.getFileId());
		file.setFileMeta(fileMeta);
		file.move(0, where);
		byteContent = file.read(length);
		
		try {
			fileData = new String(byteContent, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ErrorCode(6);
		}
		System.out.println(fileData);
		
	}

}
