package main.java.application;

import main.java.constant.FileConstant;
import main.java.fileControl.FileID;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public class CopyFileCmd extends Command {
	public CopyFileCmd(String[] cmds) {
		//1. 检查用户指令输入的的正确性（参数个数以及文件是否存在）
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
		
		//检查source file是否存在
		if(FileUtil.exists(srcFilename + FileConstant.META_SUFFIX, new StringBuilder())) {
			srcFileMeta = findFile(srcFilename);
		} else {
			throw new RuntimeException("source file not exists");
		}
		
		//检查dest file是否已创建
		//觉得有点问题，我觉得应该放在fm里
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
		//先从fm的集合里查找
		StringBuilder path = new StringBuilder();
		FileMeta fileMeta = null;
		byte[] serializedContent;
		if(FileUtil.exists(filename + FileConstant.META_SUFFIX, path)) {
			//获得filemeta文件内容
			serializedContent = FileUtil.reads(path.toString());
			System.out.println(path.toString());
			fileMeta = SerializeUtil.deserialize(FileMeta.class, serializedContent);
			return fileMeta;
		} else {
			throw new RuntimeException(filename + ": file not exists");
		}
	}
	
	//为新建文件分配fm
	static String allocFm() {
		int fmIndex = (int)(Math.random() * FileConstant.FM_COUNT + 1);
		String fmId = "fm-" + fmIndex;
		FileManagerImpl fm;
		
		//查询分配的fm对象是否已创建
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
