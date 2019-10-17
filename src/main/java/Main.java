package main.java;

import java.util.Scanner;

import main.java.application.Command;
import main.java.application.CreateFileCmd;
import main.java.util.FileUtil;


public class Main {
	public static void main(String args[]) {
		System.out.println("Start to use ¦Á-FileSystem...");
		while(true) {
			System.out.print("AFS>");
			Scanner reader = new Scanner(System.in);
			String[] inputCmd = reader.nextLine().split(" ");
//			for(int i = 0; i < inputCmd.length; i++) {
//				System.out.println(inputCmd[i] + ",");
//			}
			Command cmd;
			if(inputCmd.length == 0 || inputCmd[0] == "\n") {
				//do nothing
			} else if(inputCmd.length == 1) {
				if("exit".equals(inputCmd[0])) {
					break;
				}
			} else if(inputCmd.length == 2) {
				switch(inputCmd[0]) {
					case "alpha-create":
						String filename = inputCmd[1];
						try {
							cmd = new CreateFileCmd(filename);			
						} catch(RuntimeException e) {
							System.out.println(filename + ": " + e.getMessage());
						}
				}
			} else if(inputCmd.length == 3) {
				System.out.println("3 args input");
			} else {
				System.out.println(inputCmd[0] + ": command not found");
			}
		}
		
		//System.out.println(FileUtil.createFile("fm/fm-1/haha.meta"));
		//System.out.println(FileUtil.exsits("haha"));
	}
}
