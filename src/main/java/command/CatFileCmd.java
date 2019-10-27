package main.java.command;

import java.io.UnsupportedEncodingException;

import main.java.exception.ErrorCode;
import main.java.file.FileImpl;
import main.java.file.FileManagerImpl;
import main.java.file.FileMeta;
import main.java.id.StringId;

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
		FileImpl file = null;
		String fileData = "";
		byte[] byteContent = null;
		String fmStringId = "";
		FileManagerImpl fm = null;
		long oldPointer;

		try{
			fmStringId = findFm(filename);
			fm = getFmById(new StringId(fmStringId));
			file = (FileImpl)fm.getFile(new StringId(filename));
			oldPointer = file.pos();
			file.move(0, 1);
			byteContent = file.read((int)file.size());
			//�ָ�cat֮ǰ��ָ��λ��
			file.move(oldPointer, 1);
		} catch (ErrorCode e) {
			throw new ErrorCode(e.getErrorCode());
		}

		try {
			fileData = new String(byteContent, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ErrorCode(6);
		}

		System.out.println(fileData);

	}

}
