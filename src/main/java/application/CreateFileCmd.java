package main.java.application;

import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
import main.java.fileControl.FileManagerImpl;
import main.java.id.StringId;
import main.java.util.FileUtil;

public class CreateFileCmd extends Command {
	static FileManagerImpl fm;
	public CreateFileCmd(String[] cmds) throws Exception {
		if(cmds.length != 2) {
			throw new ErrorCode(7);
		} else {
			createFile(cmds[1]);
		}
	}
	
	public static void createFile(String filename) throws Exception {
		// 1. 在工作目录下遍历目录，检查fileName是否存在，若存在则报错（Error code）
		// 2. 若filename不存在，则随意分配一个fm，遍历工作目录，为文件挑选一个合适的fm
		// 3. 调用fm的newFile()函数创建文件
		if(FileUtil.exists(filename + FileConstant.META_SUFFIX, new StringBuilder())) {
			throw new ErrorCode(3);
		} else {
			//查询分配的fm对象是否已创建
			fm = new FileManagerImpl(allocFm());
			try {
				fm.newFile(new StringId(filename));
				//在这里维护一个hashmap 保存所有已经创建对象的FileManager
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
}
