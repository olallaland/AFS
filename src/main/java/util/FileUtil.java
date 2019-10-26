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
		for(File f : files) {                //����File[]����
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
		for(File f : files) {                //����File[]����
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
		//����Դ
        File dest = new File(destFilePath);//Ŀ�ĵأ����ļ�
        //src�ֽ������Ѿ�����
        //ѡ����
        InputStream is = null;//ByteArrayInputStream�ĸ���
        OutputStream os = null;
        //����
        try {
            is = new ByteArrayInputStream(bytes);//�ֽ����������֮��Ĺܵ�
            os = new FileOutputStream(dest);//���������ļ�֮��Ĺܵ�
            //һ�����ֽ����黺�����
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
            if(null != os) {//�ر��ļ���
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
		//����Դ
        File dest = new File(destFilePath);//Ŀ�ĵأ����ļ�
        //src�ֽ������Ѿ�����
        //ѡ����
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream os = null;
        InputStream is = null;//ByteArrayInputStream�ĸ���
        //OutputStream os = null;
        //����
        try {
            is = new ByteArrayInputStream(bytes);//�ֽ����������֮��Ĺܵ�
            os = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(
                    		destFilePath)));
            //os = new DataOutputStream(dest);//���������ļ�֮��Ĺܵ�
            //һ�����ֽ����黺�����
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
            if(null != os) {//�ر��ļ���
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
		//����Դ��Ŀ�ĵ�
        File src = new File(targetFilename);//����ļ���Դͷ�����Ŀ�ʼ����(Դ)
        byte[] dest = null;//������ڴ����γɵ��ֽ�����(Ŀ�ĵ�)
        //ѡ����
        InputStream is = null;//��	�����ļ��������������
        ByteArrayOutputStream baos= null;//�����ǳ������ļ��������
        //����(�������)
        try {
            is = new FileInputStream(src);//�ļ�������
            baos = new ByteArrayOutputStream();//�ֽ������������Ҫָ���ļ����ڴ��д���
            byte[] flush = new byte[1024*10];//���û��壬�������ڴ��䣬�����ߴ���Ч��
            int len = -1;//����ÿ�δ���ĸ���,��û�л�������ݴ��򷵻�ʣ�µ����ݣ�û�����ݷ���-1
            while((len = is.read(flush)) != -1) {
                baos.write(flush,0,len);//ÿ�ζ�ȡlen�������ݺ󣬽���д��
            }
            baos.flush();//ˢ�¹ܵ�����
            dest = baos.toByteArray();//���ջ�õ��ֽ�����
            return dest;//����baos���ڴ������γɵ��ֽ�����
        } catch (FileNotFoundException e) {
        	throw new ErrorCode(4);
        } catch (IOException e) {
        	throw new ErrorCode(1);
        }finally {
            //�ͷ���Դ,�ļ���Ҫ�ر�,�ֽ�����������ر�
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
