package main.java.application;

import main.java.exception.ErrorCode;

public class HelpCmd extends Command {
	public HelpCmd(String[] cmds) {
		if(cmds.length != 1) {
			throw new ErrorCode(7);
		} else {
			printHelps();
		}
	}

	private void printHelps() {
		System.out.println("alpha-cat [filename]");
		System.out.println("alpha-create [filename]");
		System.out.println("alpha-copy [srcFile] [desFile]");
		System.out.println("alpha-write [filename] [where] [offset] [content]");
		System.out.println("alpha-read [filename] [where] [length]");
		System.out.println("alpha-sfs [filename] [newSize]");
		System.out.println("alpha-hex [blockId]");
	}
}
