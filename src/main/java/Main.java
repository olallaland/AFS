package main.java;

import java.io.IOException;
import java.util.Scanner;

import main.java.application.Command;
import main.java.application.CreateFileCmd;
import main.java.application.WriteFileCmd;
import main.java.util.FileUtil;


public class Main {
	
	public static void main(String args[]) throws Exception {
		System.out.println("Start to use ��-FileSystem...");
		while(true) {
			System.out.print("AFS> ");
			Scanner reader = new Scanner(System.in);
			String[] inputCmd = reader.nextLine().trim().split(" ");
//			for(int i = 0; i < inputCmd.length; i++) {
//				System.out.println(inputCmd[i] + ",");
//			}
			Command cmd;
			if(inputCmd.length == 0 || inputCmd[0] == "\n") {
				//do nothing
			} else {
				switch(inputCmd[0]) {
				case "alpha-create" :
					try {
						cmd = new CreateFileCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e);
					}
					break;
				
				case "alpha-write" :
					try {
						cmd = new WriteFileCmd(inputCmd);
					} catch(RuntimeException e) {
						System.out.println(e);
					}
					break;
					
				case "alpha-cat" :
					System.out.println("get file content");
					break;
					
				case "alpha-hex" :
					System.out.println("print blockData in hex");
					break;
				
				case "alpha-copy" :
					System.out.println("copy file");
					break;
				
				case "exit" :
					System.exit(0);
					break;
					
				default :
					System.out.println(inputCmd[0] + ": command not found");
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
