package main.java.application;

import main.java.blockControl.BlockManagerImpl;
import main.java.fileControl.FileID;
import main.java.fileControl.FileManagerImpl;
import main.java.util.FileUtil;

public class CreateFileCmd extends Command {
	static FileManagerImpl fm;
	public CreateFileCmd(String[] cmds) throws Exception {
		if(cmds.length != 2) {
			throw new RuntimeException("Usage of Creating a file: alpha-create [-b filename]");
		} else {
			createFile(cmds[1]);
		}
	}
	public static int createFile(String filename) throws Exception {
		// 1. �ڹ���Ŀ¼�±���Ŀ¼�����fileName�Ƿ���ڣ��������򱨴�Error code��
		// 2. ��filename�����ڣ����������һ��fm����������Ŀ¼��Ϊ�ļ���ѡһ�����ʵ�fm
		// 3. ����fm��newFile()���������ļ�
		if(FileUtil.exists(filename, new StringBuilder())) {
			throw new RuntimeException("file exsits");
		} else {
			//��ѯ�����fm�����Ƿ��Ѵ���
			if(currFileManagers.get(allocFm()) != null) {
				fm = currFileManagers.get(allocFm());
			} else {
				fm = new FileManagerImpl(allocFm());
				currFileManagers.put(allocFm(), fm);
			}
			
			FileID fileId = new FileID(filename);
			try {
				fm.newFile(fileId);
				//������ά��һ��hashmap ���������Ѿ����������FileManager
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("The user wants to create a file named " + filename);
		return 0;
	}
	
	//Ϊ�½��ļ�����fm
	static String allocFm() {
		return "fm-2";
	}
}
