package main.java.fileControl;

import main.java.fileControl.Id;

public interface FileManager {
	File getFile(Id fileId);
	File newFile(Id fileId) throws Exception;
}
