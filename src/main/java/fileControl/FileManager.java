package main.java.fileControl;

public interface FileManager {
	File getFile(Id fileId);
	File newFile(Id fileId);
}
