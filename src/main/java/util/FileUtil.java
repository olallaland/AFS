package main.java.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import main.java.constant.InfoConstant;
import main.java.constant.FileConstant;

public class FileUtil {
	public static int createFile(String destFileName) { //destFilename = fm or bm + fm\bm id + filename
		String destFilePath = FileConstant.CWD + destFileName;
		System.out.println(destFilePath);
        File file = new File(destFilePath);
        if (file.exists()) {
            return -1; //file exsits 
        } else {
            if (destFileName.endsWith(File.separator)) {
                throw new RuntimeException(MessageFormat.format(InfoConstant.FILE_CANNOT_BE_DIR, destFileName));
            }
            // file exists?
            if (!file.getParentFile().exists()) {
                //if the parent dir is not exist, then create it.
                if (!file.getParentFile().mkdirs()) {
                    throw new RuntimeException(InfoConstant.FAILED_CREAT_DIR);
                }
            }
            // create target file.
            try {
                if (file.createNewFile()) {             
                    return 1;
                } else {
                    throw new RuntimeException(MessageFormat.format(InfoConstant.FAILED_TO_CREATE_FILE, destFileName));
                }
            } catch (IOException e) {
                throw new RuntimeException(MessageFormat.format(InfoConstant.FAILED_TO_CREATE_FILE_REASON, destFileName,
                        e.getMessage()));
            }
        }
    }
	
	public static boolean exsits(String filename) {
		String target = filename + FileConstant.FILEMETA_SUFFIX;
		System.out.println(target);
		File fmFolder = new File(FileConstant.FM_CWD);
		File[] files = fmFolder.listFiles();
		for(File f : files) {                //±éÀúFile[]Êý×é
            if (f.isDirectory()) {  
                File[] subFiles = f.listFiles();
                for(File sf : subFiles) {
                	
                	String tempFile = sf.getName();
                	System.out.println(tempFile);
                	if(tempFile.equals(target)) {
                		return true;
                	}
                }
            }
            if(f.isFile()) {
            	//
            }
        }
		return false;
	}
	
}
