package main.java.command;

import java.io.UnsupportedEncodingException;

import main.java.exception.ErrorCode;
import main.java.file.FileImpl;
import main.java.file.FileManagerImpl;
import main.java.file.FileMeta;
import main.java.id.StringId;

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
		String filename = cmds[1];
		String content = "";
		FileMeta fileMeta = null;
		String fmStringId = "";
		FileManagerImpl fm = null;
		FileImpl file;
	
		try {
			//���ָ���ƶ��ĳ�ʼλ��
			where = Integer.valueOf(cmds[2]);
			//���ָ���ƶ���ƫ����
			offset = Long.valueOf(cmds[3]);
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
		System.out.println("Ҫд������ݣ�" + content);
	
		//2. �����û�ָ���������󣬸����û������filename���ҵ���Ӧ��fileMeta�ļ�
		//���������ݷ����л������ɶ�Ӧ��fileMeta����
		try {
			fmStringId = findFm(filename);
			fm = getFmById(new StringId(fmStringId));
			file = (FileImpl) fm.getFile(new StringId(filename));
			file.move(offset, where);
			file.write(content.getBytes("utf-8"));
		} catch(ErrorCode e) {
			throw new ErrorCode(e.getErrorCode());
		}

		return 0;
	}
}
