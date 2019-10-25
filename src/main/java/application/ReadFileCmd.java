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
		//1. ����û�ָ������ĵ���ȷ�ԣ����������Լ��ļ��Ƿ���ڣ�
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
			where = Integer.valueOf(cmds[2]);//��ö�ȡ����ʼλ��
			length = Integer.valueOf(cmds[3]);//���Ҫ��ȡ�ĳ���
		} catch(Exception e ) {
			System.out.println("ת������" + e);
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
		//�ȴ�fm�ļ��������
		StringBuilder path = new StringBuilder();
		byte[] serializedContent;
		if(FileUtil.exists(filename + FileConstant.META_SUFFIX, path)) {
			//���filemeta�ļ�����
			serializedContent = FileUtil.reads(path.toString());
			System.out.println(path.toString());
			FileMeta fileMeta = SerializeUtil.deserialize(FileMeta.class, serializedContent);
			return fileMeta;
		} else {
			throw new RuntimeException(filename + ": file not exists");
		}
	}
	
	// ����fmId���FileManager����
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
