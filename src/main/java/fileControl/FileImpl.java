package main.java.fileControl;


class FileImpl implements File {
	String fileData;
	FileMeta fileMeta;
	
	public FileImpl(FileMeta fileMeta) {
		this.fileMeta = fileMeta;
	}
	@Override
	public Id getFileId() {
		return fileMeta.fileId;
	}

	@Override
	public FileManager getFileManager() {
		return new FileManagerImpl(fileMeta.fmId);
	}

	@Override
	public byte[] read(int length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(byte[] b) {
		// TODO Auto-generated method stub

	}

	@Override
	public long move(long offset, int where) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSize(long newSize) {
		// TODO Auto-generated method stub

	}

}
