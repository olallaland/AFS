package main.java.application;

import java.io.UnsupportedEncodingException;

import main.java.constant.FileConstant;
import main.java.fileControl.FileImpl;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class ReadFileCmd extends Command {
	public ReadFileCmd(String[] cmds) {
		//1. 检查用户指令输入的的正确性（参数个数以及文件是否存在）
		if(cmds.length != 4) {
			throw new RuntimeException("Usage of alpha-read: alpha-cat [filename] [where] [length]"); 
		} else {
			try {
				readFileContent(cmds);
			} catch(Exception e) {
				System.out.println(e);
			}
		}	
	}

	private void readFileContent(String[] cmds) {
		int where = 0;
		int length = 0;
		String filename;
		byte[] byteContent;
		String fileData = "";
		
		try {
			where = Integer.valueOf(cmds[2]);//获得读取的起始位置
			length = Integer.valueOf(cmds[3]);//获得要读取的长度
		} catch(Exception e ) {
			System.out.println("转化数字" + e);
		}
		
		filename = cmds[1];
		FileMeta fileMeta = findFile(filename);
		System.out.println(fileMeta);
		FileManagerImpl fm = getFmById(fileMeta.getFmId());
		FileImpl file = (FileImpl) fm.getFile(fileMeta.getFileId());
		file.setFileMeta(fileMeta);
		file.move(0, where);
		byteContent = file.read(length);
		
		try {
			fileData = new String(byteContent, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(fileData);
		
	}
	
	static FileMeta findFile(String filename) {
		//先从fm的集合里查找
		StringBuilder path = new StringBuilder();
		byte[] serializedContent;
		if(FileUtil.exists(filename + FileConstant.META_SUFFIX, path)) {
			//获得filemeta文件内容
			serializedContent = FileUtil.reads(path.toString());
			System.out.println(path.toString());
			FileMeta fileMeta = SerializeUtil.deserialize(FileMeta.class, serializedContent);
			return fileMeta;
		} else {
			throw new RuntimeException(filename + ": file not exists");
		}
	}
	
	// 根据fmId获得FileManager对象
	static FileManagerImpl getFmById(String fmId) {
		FileManagerImpl fm = null;
		try {
			if (currFileManagers.containsKey(fmId)) {
				fm = currFileManagers.get(fmId);
				System.out.println("not new fm !!!");
			} else {
				fm = new FileManagerImpl(fmId);
				System.out.println("new fm !!!");
				currFileManagers.put(fmId, fm);
			}
		} catch (RuntimeException e) {
			System.out.println("find fm error: " + e);
		}

		return fm;
	}
}
