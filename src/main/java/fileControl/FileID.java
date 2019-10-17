package main.java.fileControl;

public class FileID implements Id {
	private FMID fmID;
	private String filename;
	
	public FileID(String filename) {
		this.filename = filename;
	}
	
	public String toString() {
		return filename;
	}
}
