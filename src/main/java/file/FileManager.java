package main.java.file;

import main.java.id.Id;

public interface FileManager {
	File getFile(Id fileId) throws Exception;
	File newFile(Id fileId) throws Exception;
}
