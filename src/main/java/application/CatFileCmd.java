package main.java.application;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import main.java.blockControl.BlockImpl;
import main.java.blockControl.BlockMeta;
import main.java.constant.FileConstant;
import main.java.fileControl.FileImpl;
import main.java.fileControl.FileMeta;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class CatFileCmd extends Command {
	public CatFileCmd(String[] cmds) {
		//1. 检查用户指令输入的的正确性（参数个数以及文件是否存在）
		if(cmds.length != 2) {
			throw new RuntimeException("Usage of alpha-cat: alpha-cat [filename]"); 
		} else {
			try {
				catFileContent(cmds[1]);
			} catch(Exception e) {
				System.out.println(e);
			}
		}	
	}
	
	public static int catFileContent(String filename) {
		FileMeta fileMeta;
		String blockId = "";
		String bmId = "";
		String dataPath = "";
		String metaPath = "";
		String fileData = "";
		byte[] byteContent;
		
		fileMeta = findFile(filename);
			
		//2. 根据得到的FileMeta创建file对象获得文件FileData
		System.out.println(fileMeta);
		FileImpl file = new FileImpl(fileMeta);
		byteContent = file.read((int)fileMeta.getFileSize());
		
		try {
			fileData = new String(byteContent, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(fileData);
		return 0;
	}
	
	static FileMeta findFile(String filename) {
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
}
