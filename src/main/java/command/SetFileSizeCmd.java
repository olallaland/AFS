package main.java.command;

import main.java.exception.ErrorCode;
import main.java.file.FileImpl;
import main.java.file.FileManagerImpl;
import main.java.id.StringId;

public class SetFileSizeCmd extends Command {
	public SetFileSizeCmd(String[] cmds) throws Exception {
		if(cmds.length != 3) {
			throw new ErrorCode(7);
		} else {
			try {
				setFileSize(cmds);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void setFileSize(String[] cmds) {
		String filename = cmds[1];
		long length = 0;
		String fmStringId = "";
		FileManagerImpl fm;
		FileImpl file;
		long oldPointer;

		try {
			length = Long.valueOf(cmds[2]);
		} catch (Exception e) {
			throw new ErrorCode(8);
		}
		if(length < 0) {
			throw new ErrorCode(17);
		}

		try {
			fmStringId = findFm(filename);
			fm = getFmById(new StringId(fmStringId));
			file = (FileImpl)fm.getFile(new StringId(filename));
			oldPointer = file.pos();
			file.move(0, 1);
			file.setSize(length);
			//恢复cat之前的指针位置
			file.move(oldPointer, 1);
		} catch(ErrorCode e) {
			throw new ErrorCode(e.getErrorCode());
		}

	}
}
