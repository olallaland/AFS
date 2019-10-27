package main.java.command;

import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
import main.java.file.FileImpl;
import main.java.file.FileManagerImpl;
import main.java.file.FileMeta;
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
		String srcFmStringId = "";
		String destFmStringId = "";
		String srcFilename = cmds[1];
		String destFilename = cmds[2];
		FileMeta srcFileMeta;
		FileMeta destFileMeta = new FileMeta();
		FileManagerImpl srcFm = null;
		FileManagerImpl destFm = null;
		FileImpl srcFile = null;
		FileImpl destFile = null;

		try {
			srcFmStringId = findFm(srcFilename);
			srcFm = getFmById(new StringId(srcFmStringId));
			srcFile = (FileImpl)srcFm.getFile(new StringId(srcFilename));
		} catch (ErrorCode e) {
			throw new ErrorCode(e.getErrorCode());
		}

		//检查dest file是否已创建
		if(!FileUtil.exists(destFilename + FileConstant.META_SUFFIX, new StringBuilder())) {
			destFm = new FileManagerImpl(allocFm());
			try {
				destFile = (FileImpl) destFm.newFile(new StringId(destFilename));
				destFm.addFileToList(destFilename);
				srcFile.copy(destFile);
			} catch (ErrorCode e) {
				throw new ErrorCode(e.getErrorCode());
			}
		} else {
			throw new ErrorCode(3);
		}

	}
}
