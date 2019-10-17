package main.java.application;

import main.java.fileControl.FileID;
import main.java.fileControl.FileManagerImpl;
import main.java.util.FileUtil;

public class CreateFileCmd extends Command {

	public CreateFileCmd(String filename) throws Exception {
		createFile(filename);
	}
	public static int createFile(String filename) throws Exception {
		// 1. 在工作目录下遍历目录，检查fileName是否存在，若存在则报错（Error code）
		// 2. 若filename不存在，则随意分配一个fm，遍历工作目录，为文件挑选一个合适的fm
		// 3. 调用fm的newFile()函数创建文件
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
	
	//为新建文件分配fm
	static FileManagerImpl allocFm() {
		return new FileManagerImpl("fm-2");
	}
}
