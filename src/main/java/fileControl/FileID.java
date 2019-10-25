package main.java.fileControl;

import java.io.Serializable;

import main.java.fileControl.Id;

public class FileID implements Id, Serializable {
	private FMID fmID;
	private String filename;
	
	public FileID(String filename) {
		this.filename = filename;
	}
	
	@Override
	public String toString() {
		return filename;
	}

}
