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
     * ���л�ID
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
		FileImpl file = new FileImpl(fileMeta);//������ά��һ��hashmap ���������Ѿ����������File
		fileSet.put(id, file);
		//fm.write(destFilename);
		//fileId��Ϊ����Ҫ������file������ ����û��Ŀ¼�ṹ�����Բ�����ͬ�� fileId��Ψһ��
		// 1. ����Ƿ�����ΪfileId���ļ������� ��FileUtil��ѯ���ϲ��飿��
		// 2. ����fmId�ڶ�ӦĿ¼�´�����Ӧ��meta�ļ�
		return file;
	}

}
