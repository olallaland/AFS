package main.java.fileControl;

import java.io.Serializable;
import java.util.HashMap;

import main.java.id.Id;
import main.java.id.StringId;

public class FileManagerImpl implements FileManager, Serializable {
	Id fmId;
	HashMap<String, FileImpl> fileSet;
	
	public FileManagerImpl(Id fmId) {
		this.fmId = fmId;
		fileSet = new HashMap<String, FileImpl>();
	}
	
	public Id getFmId() {
		return this.fmId;
	}
	
	public String getStringFmId() {
		if(fmId instanceof StringId) {
			StringId sid = (StringId) fmId;
			String id = sid.getId();
			return id;
		} else {
			//TODO
			return "";
		}
	}
	/**
     * 序列化ID
     */
    private static final long serialVersionUID = -5809782578272943999L;
	@Override
	public File getFile(Id fileId) {
		if(fileId instanceof StringId) {
			StringId sid = (StringId) fileId;
			String id = sid.getId();
		
			if(fileSet.containsKey(id)) {
				System.out.println("not new file");
				return fileSet.get(id);
			} else {
				System.out.println(" new file!!!");
				FileImpl file = new FileImpl(fileId);
				fileSet.put(id, file);
				return file;
			}
		}
		return null;
	}

	@Override
	public File newFile(Id fileId) throws Exception {
		String id = "";
		if(fileId instanceof StringId) {
			StringId sid = (StringId) fileId;
			id = sid.getId();	
		} else {
			
		}
		FileMeta fileMeta = new FileMeta(fileId, fmId);
		FileImpl file = new FileImpl(fileMeta);//在这里维护一个hashmap 保存所有已经创建对象的File
		fileSet.put(id, file);
		//fm.write(destFilename);
		//fileId即为输入要创建的file的名字 由于没有目录结构，所以不允许同名 fileId是唯一的
		// 1. 检查是否有名为fileId的文件创建了 用FileUtil查询（上层检查？）
		// 2. 根据fmId在对应目录下创建对应的meta文件
		return file;
	}

}
