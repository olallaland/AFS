package main.java.application;

import java.io.UnsupportedEncodingException;

import main.java.exception.ErrorCode;
import main.java.fileControl.FileImpl;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;

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
		String filename;
		byte[] byteContent;
		String fileData = "";
		FileMeta fileMeta = null;
		
		try {
			where = Integer.valueOf(cmds[2]);//获得读取的起始位置
			length = Integer.valueOf(cmds[3]);//获得要读取的长度
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
