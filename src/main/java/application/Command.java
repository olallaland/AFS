package main.java.application;

import java.util.HashMap;

import main.java.blockControl.BlockManagerImpl;
import main.java.fileControl.FileManagerImpl;

public abstract class Command {
	
	public static HashMap<String, BlockManagerImpl> currBlockManagers = 
			new HashMap<String, BlockManagerImpl>();
	public static HashMap<String, FileManagerImpl> currFileManagers = 
			new HashMap<String, FileManagerImpl>();
	Command(String cmdName, String filename) {
		
	}
	
	Command(String filename) {
		
	}
	Command() {
		
	}
	
}
