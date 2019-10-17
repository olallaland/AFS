package main.java.application;

import main.java.fileControl.FileID;
import main.java.fileControl.FileManagerImpl;
import main.java.util.FileUtil;

public class CreateFileCmd extends Command {

	public CreateFileCmd(String filename) throws Exception {
		createFile(filename);
	}
	public static int createFile(String filename) throws Exception {
		// 1. �ڹ���Ŀ¼�±���Ŀ¼�����fileName�Ƿ���ڣ��������򱨴�Error code��
		// 2. ��filename�����ڣ����������һ��fm����������Ŀ¼��Ϊ�ļ���ѡһ�����ʵ�fm
		// 3. ����fm��newFile()���������ļ�
		if(FileUtil.exsits(filename)) {
			throw new RuntimeException("file exsits");
		} else {
			FileManagerImpl fm = allocFm();
			FileID fileId = new FileID(filename);
			try {
				fm.newFile(fileId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("The user wants to create a file named " + filename);
		return 0;
	}
	
	//Ϊ�½��ļ�����fm
	static FileManagerImpl allocFm() {
		return new FileManagerImpl("fm-2");
	}
}
