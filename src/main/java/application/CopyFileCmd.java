package main.java.application;

import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;
import main.java.id.StringId;
import main.java.util.FileUtil;

public class CopyFileCmd extends Command {
	public CopyFileCmd(String[] cmds) {
		//1. 检查用户指令输入的的正确性（参数个数以及文件是否存在）
		if(cmds.length != 3) {
			throw new ErrorCode(7); 
		} else {
			try {
				copyFile(cmds);
			} catch(Exception e) {
				System.out.println(e.getMessage());
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
			throw new ErrorCode(4);
		}
		
		//检查dest file是否已创建
		//觉得有点问题，我觉得应该放在fm里
		if(!FileUtil.exists(destFilename + FileConstant.META_SUFFIX, new StringBuilder())) {
			fm = new FileManagerImpl(allocFm());
			destFileMeta.setBlockCount(srcFileMeta.getBlockCount());
			destFileMeta.setFmId(new StringId(fm.getStringFmId()));
			destFileMeta.setFileId(new StringId(destFilename));
			destFileMeta.setFileSize(srcFileMeta.getFileSize());
			destFileMeta.setLogicBlocks(srcFileMeta.getLogicBlocks());
			
			System.out.println(destFileMeta);
			System.out.println(srcFileMeta);
			try {
				destFileMeta.write();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			throw new ErrorCode(3);
		}
	}

}
