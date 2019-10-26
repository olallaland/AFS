package main.java.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;

import main.java.constant.FileConstant;
import main.java.constant.InfoConstant;
import main.java.exception.ErrorCode;

public class FileUtil {
	public static int createFile(String destFilePath) { //destFilename = fm or bm + fm\bm id + filename
		System.out.println(destFilePath);
        File file = new File(destFilePath);
        if (file.exists()) {
            return -1; //file exists 
        } else {
            if (destFilePath.endsWith(File.separator)) {
                throw new RuntimeException(MessageFormat.format(InfoConstant.FILE_CANNOT_BE_DIR, destFilePath));
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
                }
            
            } catch (IOException e) {
                throw new ErrorCode(1);
            }
        }
		return 0;
    }
	
	public static boolean exists(String target, StringBuilder path) {
		//String target = filename + FileConstant.META_SUFFIX;
		//System.out.println(target);
		File fmFolder = new File(FileConstant.FM_CWD);
		File[] files = fmFolder.listFiles();
		for(File f : files) {                //遍历File[]数组
            if (f.isDirectory()) {  
                File[] subFiles = f.listFiles();
                for(File sf : subFiles) {
                	
                	String tempFile = sf.getName();
                	//System.out.println(tempFile);
                	if(tempFile.equals(target)) {
                		path.append(sf + "");
                		System.out.println(path);
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
	
	public static boolean blockExists(String target, StringBuilder path) {
		//String target = filename + FileConstant.META_SUFFIX;
		//System.out.println(target);
		File fmFolder = new File(FileConstant.BM_CWD);
		File[] files = fmFolder.listFiles();
		for(File f : files) {                //遍历File[]数组
            if (f.isDirectory()) {  
                File[] subFiles = f.listFiles();
                for(File sf : subFiles) {
                	
                	String tempFile = sf.getName();
                	//System.out.println(tempFile);
                	if(tempFile.equals(target)) {
                		path.append(sf + "");
                		System.out.println(path);
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
	
	public static void writes(byte[] bytes, String destFilePath) throws IOException {
		createFile(destFilePath);
		System.out.println("this is a file writes: " + destFilePath); 
		//创建源
        File dest = new File(destFilePath);//目的地，新文件
        //src字节数组已经存在
        //选择流
        InputStream is = null;//ByteArrayInputStream的父类
        OutputStream os = null;
        //操作
        try {
            is = new ByteArrayInputStream(bytes);//字节数组与程序之间的管道
            os = new FileOutputStream(dest);//程序与新文件之间的管道
            //一样的字节数组缓冲操作
            byte[] flush = new byte[1024*10];
            int len = -1;
                while((len = is.read(flush)) != -1) {
                    os.write(flush,0,len);
                }
                os.flush();
        } catch (FileNotFoundException e) {
            throw new ErrorCode(4);
        } catch (IOException e) {
            throw new ErrorCode(1);
        }finally {
            if(null != os) {//关闭文件流
                try {
                    os.close();
                } catch (IOException e) {
                    throw new ErrorCode(1);
                }
            }
        }
    
	}
	
	/*
	public static void writeInBinary(byte[] bytes, String destFilePath) {
		createFile(destFilePath);
		System.out.println("this is a file writes in Binary: " + destFilePath); 
		//创建源
        File dest = new File(destFilePath);//目的地，新文件
        //src字节数组已经存在
        //选择流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream os = null;
        InputStream is = null;//ByteArrayInputStream的父类
        //OutputStream os = null;
        //操作
        try {
            is = new ByteArrayInputStream(bytes);//字节数组与程序之间的管道
            os = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(
                    		destFilePath)));
            //os = new DataOutputStream(dest);//程序与新文件之间的管道
            //一样的字节数组缓冲操作
            byte[] flush = new byte[1024*10];
            int len = -1;
                while((len = is.read(flush)) != -1) {
                    os.write(flush,0,len);
                }
                os.flush();
        } catch (FileNotFoundException e) {
            throw new ErrorCode(4);
        } catch (IOException e) {
            throw new ErrorCode(1);
        }finally {
            if(null != os) {//关闭文件流
                try {
                    os.close();
                } catch (IOException e) {
                	throw new ErrorCode(1);
                }
            }
        }
	}
	
	*/
	
	public static byte[] reads(String targetFilename) {
		//创建源与目的地
        File src = new File(targetFilename);//获得文件的源头，从哪开始传入(源)
        byte[] dest = null;//最后在内存中形成的字节数组(目的地)
        //选择流
        InputStream is = null;//此	流是文件到程序的输入流
        ByteArrayOutputStream baos= null;//此流是程序到新文件的输出流
        //操作(输入操作)
        try {
            is = new FileInputStream(src);//文件输入流
            baos = new ByteArrayOutputStream();//字节输出流，不需要指定文件，内存中存在
            byte[] flush = new byte[1024*10];//设置缓冲，这样便于传输，大大提高传输效率
            int len = -1;//设置每次传输的个数,若没有缓冲的数据大，则返回剩下的数据，没有数据返回-1
            while((len = is.read(flush)) != -1) {
                baos.write(flush,0,len);//每次读取len长度数据后，将其写出
            }
            baos.flush();//刷新管道数据
            dest = baos.toByteArray();//最终获得的字节数组
            return dest;//返回baos在内存中所形成的字节数组
        } catch (FileNotFoundException e) {
        	throw new ErrorCode(4);
        } catch (IOException e) {
        	throw new ErrorCode(1);
        }finally {
            //释放资源,文件需要关闭,字节数组流无需关闭
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                	throw new ErrorCode(1);
                }
            }
            
        }
	}
	
	public static int readIdCount() throws IOException {
		File file = new File(FileConstant.ID_COUNT_PATH);
		int ch;
		String temp = "";
        if (file.exists()) {
        	FileReader input = new FileReader(file);
        	while((ch = input.read()) != -1) {
        		temp += (char)ch;
        	}
        	input.close();
            System.out.println(temp);
            return Integer.parseInt(temp); //file exsits 
        } else {
        	updateIdCount(1);
        	return 1;  	
        }
	}
	
	public static int updateIdCount(int newValue) throws IOException {
		File file = new File(FileConstant.ID_COUNT_PATH);
		FileWriter output;
		if (!file.exists()) {

			if (!file.getParentFile().exists()) {
				// if the parent dir is not exist, then create it.
				if (!file.getParentFile().mkdirs()) {
					throw new RuntimeException(InfoConstant.FAILED_CREAT_DIR);
				}
			}
			// create target file.
			try {
				file.createNewFile();
			
			} catch (IOException e) {
				throw new ErrorCode(1);
			}
		}
		output = new FileWriter(file);
		output.write(newValue + "");
		output.flush();
		output.close();
		return 0;
	}
		
}
