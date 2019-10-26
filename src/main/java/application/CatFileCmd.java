package main.java.application;

import java.io.UnsupportedEncodingException;

import main.java.exception.ErrorCode;
import main.java.fileControl.FileImpl;
import main.java.fileControl.FileMeta;

public class CatFileCmd extends Command {
	public CatFileCmd(String[] cmds) {
		//1. ����û�ָ������ĵ���ȷ�ԣ����������Լ��ļ��Ƿ���ڣ�
		if(cmds.length != 2) {
			throw new ErrorCode(7); 
		} else {
			try {
				catFileContent(cmds[1]);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}	
	}
	
	public static void catFileContent(String filename) {
		FileMeta fileMeta = null;
		String fileData = "";
		byte[] byteContent = null;
		try {
			fileMeta = findFile(filename);
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
		}
		//2. ���ݵõ���FileMeta����file�������ļ�FileData
		System.out.println(fileMeta);
		FileImpl file = new FileImpl(fileMeta);
		try {
			byteContent = file.read((int)fileMeta.getFileSize());
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
		}
		
		try {
			fileData = new String(byteContent, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ErrorCode(6);
		}
		System.out.println(fileData);
	}

}
