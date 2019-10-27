package main.java.command;

import java.io.UnsupportedEncodingException;

import main.java.exception.ErrorCode;
import main.java.file.FileImpl;
import main.java.file.FileManagerImpl;
import main.java.file.FileMeta;
import main.java.id.StringId;

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
		String filename = cmds[1];
		String content = "";
		FileMeta fileMeta = null;
		String fmStringId = "";
		FileManagerImpl fm = null;
		FileImpl file;
	
		try {
			//获得指针移动的初始位置
			where = Integer.valueOf(cmds[2]);
			//获得指针移动的偏移量
			offset = Long.valueOf(cmds[3]);
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
		System.out.println("要写入的内容：" + content);
	
		//2. 若是用户指令输入无误，根据用户输入的filename查找到对应的fileMeta文件
		//并将其内容反序列化，生成对应的fileMeta对象
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
