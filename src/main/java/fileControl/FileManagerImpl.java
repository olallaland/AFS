package main.java.fileControl;

import java.io.Serializable;

import main.java.constant.FileConstant;
import main.java.util.FileUtil;

public class FileManagerImpl implements FileManager, Serializable {
	String fmId;
	File[] fileSets;
	public FileManagerImpl(String fmId) {
		this.fmId = fmId;
	}
	/**
     * ���л�ID
     */
    private static final long serialVersionUID = -5809782578272943999L;
	@Override
	public File getFile(Id fileId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File newFile(Id fileId) {
		String destFilename = "fm" + FileConstant.PATH_SEPARATOR + fmId +
				FileConstant.PATH_SEPARATOR + fileId.toString() + FileConstant.FILEMETA_SUFFIX;
		FileUtil.createFile(destFilename);
		//fileId��Ϊ����Ҫ������file������ ����û��Ŀ¼�ṹ�����Բ�����ͬ�� fileId��Ψһ��
		// 1. ����Ƿ�����ΪfileId���ļ������� ��FileUtil��ѯ���ϲ��飿��
		// 2. ����fmId�ڶ�ӦĿ¼�´�����Ӧ��meta�ļ�
		return null;
	}

}
