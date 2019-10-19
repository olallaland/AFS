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
		// 1. 在工作目录下遍历目录，检查fileName是否存在，若存在则报错（Error code）
		// 2. 若filename不存在，则随意分配一个fm，遍历工作目录，为文件挑选一个合适的fm
		// 3. 调用fm的newFile()函数创建文件
		if(FileUtil.exists(filename, new StringBuilder())) {
			throw new RuntimeException("file exsits");
		} else {
			//查询分配的fm对象是否已创建
			if(currFileManagers.get(allocFm()) != null) {
				fm = currFileManagers.get(allocFm());
			} else {
				fm = new FileManagerImpl(allocFm());
				currFileManagers.put(allocFm(), fm);
			}
			
			FileID fileId = new FileID(filename);
			try {
				fm.newFile(fileId);
				//在这里维护一个hashmap 保存所有已经创建对象的FileManager
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("The user wants to create a file named " + filename);
		return 0;
	}
	
	//为新建文件分配fm
	static String allocFm() {
		return "fm-2";
	}
}
