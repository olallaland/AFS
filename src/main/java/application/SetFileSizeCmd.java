package main.java.application;

import main.java.exception.ErrorCode;
import main.java.fileControl.FileImpl;
import main.java.fileControl.FileMeta;

public class SetFileSizeCmd extends Command {
	public SetFileSizeCmd(String[] cmds) throws Exception {
		if(cmds.length != 3) {
			throw new ErrorCode(7);
		} else {
			setFileSize(cmds);
		}
	}

	private void setFileSize(String[] cmds) {
		String filename = cmds[1];
		long length = 0;
		FileMeta fileMeta = null;
		try {
			length = Long.valueOf(cmds[2]);
			fileMeta = findFile(filename);
			FileImpl file = new FileImpl(fileMeta);
			file.setSize(length);
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
		}
	
	}
}
