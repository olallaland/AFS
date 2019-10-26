package main.java.application;

import java.util.HashMap;

import main.java.blockControl.BlockManagerImpl;
import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
import main.java.fileControl.FileManagerImpl;
import main.java.fileControl.FileMeta;
import main.java.id.Id;
import main.java.id.StringId;
import main.java.util.FileUtil;
import main.java.util.SerializeUtil;

public abstract class Command {
	
	public static HashMap<String, BlockManagerImpl> currBlockManagers = 
			new HashMap<String, BlockManagerImpl>();
	public static HashMap<String, FileManagerImpl> currFileManagers = 
			new HashMap<String, FileManagerImpl>();
	Command(String cmdName, String filename) {
		
	}
	
	Command(String filename) {
		
	}
	Command() {
		
	}
	
	static FileMeta findFile(String filename) {
		StringBuilder path = new StringBuilder();
		byte[] serializedContent;
		if(FileUtil.exists(filename + FileConstant.META_SUFFIX, path)) {
			//���filemeta�ļ�����
			serializedContent = FileUtil.reads(path.toString());
			System.out.println(path.toString());
			FileMeta fileMeta = SerializeUtil.deserialize(FileMeta.class, serializedContent);
			return fileMeta;
		} else {
			throw new ErrorCode(4);
		}
	}
	
	// Ϊ�½��ļ�����fm
	static Id allocFm() {
		int fmIndex = (int) (Math.random() * FileConstant.FM_COUNT + 1);
		String sid = "fm-" + fmIndex;
		FileManagerImpl fm;

		// ��ѯ�����fm�����Ƿ��Ѵ���
		try {
			if (currFileManagers.containsKey(sid)) {
				fm = currFileManagers.get(sid);
			} else {
				fm = new FileManagerImpl(new StringId(sid));
				currFileManagers.put(sid, fm);
			}
		} catch (RuntimeException e) {
			System.out.println("allocate fm error: " + e);
		}

		System.out.println("choose a fm :" + sid);
		return new StringId(sid);
	}
	
	// ����fmId���FileManager����
	static FileManagerImpl getFmById(Id fmId) {
		String sfmId = "";
		if (fmId instanceof StringId) {
			StringId sid = (StringId) fmId;
			sfmId = sid.getId();
		} else {
			// TODO
		}
		FileManagerImpl fm = null;
		try {
			if (currFileManagers.containsKey(sfmId)) {
				fm = currFileManagers.get(sfmId);
				System.out.println("not new fm !!!");
			} else {
				fm = new FileManagerImpl(fmId);
				System.out.println("new fm !!!");
				currFileManagers.put(sfmId, fm);
			}
		} catch (RuntimeException e) {
			System.out.println("find fm error: " + e);
		}

		return fm;
	}
	
	//Ϊ�¿��Ŀ����BlockManager
	public static BlockManagerImpl allocBm() {
		int bmIndex = (int) (Math.random() * FileConstant.BM_COUNT + 1);
		String sbmId = "bm-" + bmIndex;
		BlockManagerImpl bm = null ;
		
		//��ѯ�����bm�����Ƿ��Ѵ���
		try {
			if(currBlockManagers.containsKey(sbmId)) {
				bm = currBlockManagers.get(sbmId);
			} else {
				bm = new BlockManagerImpl(new StringId(sbmId));
				currBlockManagers.put(sbmId, bm);
			}
		} catch(Exception e) {
			System.out.println("allocate bm error : " + e);
		}
		return bm;
	}
}
