package main.java.command;

import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
import main.java.file.FileManagerImpl;
import main.java.id.StringId;
import main.java.util.FileUtil;

public class CreateFileCmd extends Command {
	public CreateFileCmd(String[] cmds) throws Exception {
		if(cmds.length != 2) {
			throw new ErrorCode(7);
		} else {
			try {
				createFile(cmds[1]);
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	private static void createFile(String filename) throws Exception {
		// 1. �ڹ���Ŀ¼�±���Ŀ¼�����fileName�Ƿ���ڣ��������򱨴�Error code��
		// 2. ��filename�����ڣ����������һ��fm����������Ŀ¼��Ϊ�ļ���ѡһ�����ʵ�fm
		// 3. ����fm��newFile()���������ļ�
		FileManagerImpl fm;
		if(FileUtil.exists(filename + FileConstant.META_SUFFIX, new StringBuilder())) {
			throw new ErrorCode(3);
		} else {
			//��ѯ�����fm�����Ƿ��Ѵ���
			fm = new FileManagerImpl(allocFm());
			try {
				fm.newFile(new StringId(filename));
				fm.addFileToList(filename);
				//������ά��һ��hashmap ���������Ѿ����������FileManager
			} catch (ErrorCode e) {
				throw new ErrorCode(e.getErrorCode());
			}
		}
	}
	
}
