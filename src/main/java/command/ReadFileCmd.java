package main.java.command;

import java.io.UnsupportedEncodingException;

import main.java.exception.ErrorCode;
import main.java.file.FileImpl;
import main.java.file.FileManagerImpl;
import main.java.file.FileMeta;
import main.java.id.StringId;

public class ReadFileCmd extends Command {
	public ReadFileCmd(String[] cmds) {
		//1. 检查用户指令输入的的正确性（参数个数以及文件是否存在）
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
		String filename = cmds[1];
		byte[] byteContent;
		String fmStringId = "";
		FileManagerImpl fm = null;
		FileImpl file = null;
		String fileData = "";
		FileMeta fileMeta = null;
		
		try {
			//获得读取的起始位置
			where = Integer.valueOf(cmds[2]);
			//获得要读取的长度
			length = Integer.valueOf(cmds[3]);
		} catch(Exception e ) {
			throw new ErrorCode(8);
		}

		try {
			fmStringId = findFm(filename);
			fm = getFmById(new StringId(fmStringId));
			file = (FileImpl) fm.getFile(new StringId(filename));
			file.move(0, where);
			byteContent = file.read(length);
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
