package main.java.application;

import main.java.constant.FileConstant;
import main.java.fileControl.FileID;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class CopyFileCmd extends Command {
	public CopyFileCmd(String[] cmds) {
		//1. ����û�ָ������ĵ���ȷ�ԣ����������Լ��ļ��Ƿ���ڣ�
		if(cmds.length != 3) {
			throw new RuntimeException("Usage of alpha-read: alpha-copy [from] [to]"); 
		} else {
			try {
				copyFile(cmds);
			} catch(Exception e) {
				System.out.println(e);
			}
		}	
	}

	private void copyFile(String[] cmds) {
		String srcFilename = cmds[1];
		String destFilename = cmds[2];
		FileMeta srcFileMeta;
		FileMeta destFileMeta = new FileMeta();
		FileManagerImpl fm;
		
		//���source file�Ƿ����
		if(FileUtil.exists(srcFilename + FileConstant.META_SUFFIX, new StringBuilder())) {
			srcFileMeta = findFile(srcFilename);
		} else {
			throw new RuntimeException("source file not exists");
		}
		
		//���dest file�Ƿ��Ѵ���
		//�����е����⣬�Ҿ���Ӧ�÷���fm��
		if(!FileUtil.exists(destFilename + FileConstant.META_SUFFIX, new StringBuilder())) {
			fm = new FileManagerImpl(allocFm());
			destFileMeta.setBlockCount(srcFileMeta.getBlockCount());
			destFileMeta.setFmId(fm.getFmId());
			destFileMeta.setFileId(new FileID(destFilename));
			destFileMeta.setFileSize(srcFileMeta.getFileSize());
			destFileMeta.setLogicBlocks(srcFileMeta.getLogicBlocks());
			
			System.out.println(destFileMeta);
			System.out.println(srcFileMeta);
			try {
				destFileMeta.write();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("write back filemeta error: " + e);
			}
		} else {
			throw new RuntimeException("dest file exists");
		}
	}
	
	static FileMeta findFile(String filename) {
		//�ȴ�fm�ļ��������
		StringBuilder path = new StringBuilder();
		FileMeta fileMeta = null;
		byte[] serializedContent;
		if(FileUtil.exists(filename + FileConstant.META_SUFFIX, path)) {
			//���filemeta�ļ�����
			serializedContent = FileUtil.reads(path.toString());
			System.out.println(path.toString());
			fileMeta = SerializeUtil.deserialize(FileMeta.class, serializedContent);
			return fileMeta;
		} else {
			throw new RuntimeException(filename + ": file not exists");
		}
	}
	
	//Ϊ�½��ļ�����fm
	static String allocFm() {
		int fmIndex = (int)(Math.random() * FileConstant.FM_COUNT + 1);
		String fmId = "fm-" + fmIndex;
		FileManagerImpl fm;
		
		//��ѯ�����fm�����Ƿ��Ѵ���
		try {
			if(currFileManagers.containsKey(fmId)) {
				fm = currFileManagers.get(fmId);
			} else {
				fm = new FileManagerImpl(fmId);
				currFileManagers.put(fmId, fm);
			}
		} catch(RuntimeException e) {
			System.out.println("allocate fm error: " + e);
		}
		
		System.out.println("choose a fm :" + fmId);
		return fmId;
	}
}
