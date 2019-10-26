package main.java.application;

import java.io.UnsupportedEncodingException;

import main.java.exception.ErrorCode;
import main.java.fileControl.FileImpl;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;

public class WriteFileCmd extends Command {
	
	public WriteFileCmd(String[] cmds) throws UnsupportedEncodingException {
		//1. 检查用户指令输入的的正确性
		if(cmds.length < 5) {
			throw new ErrorCode(7);
		} else {
			//拼接被空格号隔开的内容
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
			where = Integer.valueOf(cmds[2]);//获得指针移动的初始位置
			offset = Long.valueOf(cmds[3]);//获得指针移动的偏移量
		} catch(Exception e ) {
			throw new ErrorCode(8);
		}
		
		//拼接用空格隔开的content
		for(int i = 4; i < cmds.length; i++) {
			if(i == cmds.length - 1) {
				content += cmds[i];
			} else {
				content += cmds[i] + " ";
			}
		}
		//去掉首尾的双引号
		content = content.substring(1, content.length() - 1);
		System.out.println(content);
	
		//2. 若是用户指令输入无误，根据用户输入的filename查找到对应的fileMeta文件
		//并将其内容反序列化，生成对应的fileMeta对象
		filename = cmds[1];
		//这一步移到fileManager里面
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
