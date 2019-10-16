package fileControl;

import java.io.Serializable;

public class FileManagerImpl implements FileManager, Serializable {
	String fmId;
	File[] fileSets;
	FileManagerImpl(String fmId) {
		this.fmId = fmId;
	}
	/**
     * 序列化ID
     */
    private static final long serialVersionUID = -5809782578272943999L;
	@Override
	public File getFile(Id fileId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File newFile(Id fileId) {
		//fileId即为输入要创建的file的名字 由于没有目录结构，所以不允许同名 fileId是唯一的
		// 1. 检查是否有名为fileId的文件创建了 用FileUtil查询（上层检查？）
		// 2. 根据fmId在对应目录下创建对应的meta文件
		return null;
	}

}
