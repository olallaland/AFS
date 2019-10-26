package main.java;

import java.util.Scanner;

import main.java.application.CatFileCmd;
import main.java.application.Command;
import main.java.application.CopyFileCmd;
import main.java.application.CreateFileCmd;
import main.java.application.HelpCmd;
import main.java.application.ReadBlockInHexCmd;
import main.java.application.ReadFileCmd;
import main.java.application.SetFileSizeCmd;
import main.java.application.WriteFileCmd;
import main.java.exception.ErrorCode;


public class Main {
	
	public static void main(String args[]) throws Exception {
		System.out.println("Start to use ¦Á-FileSystem...");
		while(true) {
			System.out.print("AFS> ");
			Scanner reader = new Scanner(System.in);
			String[] inputCmd = reader.nextLine().trim().split(" ");
//			for(int i = 0; i < inputCmd.length; i++) {
//				System.out.println(inputCmd[i] + ",");
//			}
			Command cmd;
			if(inputCmd[0].equals("")) {
				//do nothing
			} else {
				switch(inputCmd[0]) {
				case "alpha-create" :
					try {
						cmd = new CreateFileCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
				
				case "alpha-write" :
					try {
						cmd = new WriteFileCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
					
				case "alpha-cat" :
					try {
						cmd = new CatFileCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());	
					}
					break;
				
				case "alpha-read" :
					try {
						cmd = new ReadFileCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
					
				case "alpha-sfs" :
					try {
						cmd = new SetFileSizeCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
					
				case "alpha-hex" :
					try {
						cmd = new ReadBlockInHexCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
				
				case "alpha-copy" :
					try {
						cmd = new CopyFileCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
				
				case "help" :
					try {
						cmd = new HelpCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
					
				case "exit" :
					System.exit(0);
					break;
					
				default :
					System.out.println(inputCmd[0] + ": " + ErrorCode.getErrorText(10));
					break;
				}
			}
		}
		
		//System.out.println(FileUtil.createFile("fm/fm-1/haha.meta"));
		//System.out.println(FileUtil.exsits("haha"));
	}
	
	/*
	public static void main(String args[]) throws IOException {
		//FileUtil.updateIdCount(5162313);
		System.out.println(FileUtil.readIdCount());
		
	}
	*/
}
