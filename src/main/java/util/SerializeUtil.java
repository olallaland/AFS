package main.java.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	/**  
	 * Title: toBytes  
	 * Description:���л�����  
	 * @author zhengzx  
	 * @throws Exception 
	 */  
	public static byte[] toBytes(Object out) throws Exception {
		//���л��� ��������� --> ��ʾ��һ��Ŀ�� д������
        ObjectOutputStream objectOutputStream =null;
        //�ֽ����������
        ByteArrayOutputStream byteArrayOutputStream = null;
        try{
            //����һ��������
            byteArrayOutputStream = new ByteArrayOutputStream();
            //�� ���� ���л��� �ֽں�  ���뻺������
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            //���л� ����
            objectOutputStream.writeObject(out);
            //�� �� ���л��ֽ�
            byte[] bytes = byteArrayOutputStream.toByteArray();

            //��������
            objectOutputStream.flush();
            //�ͷ���Դ
            objectOutputStream.close();

            return bytes;
        }catch (Exception e){
            System.out.println("�����쳣:"+e.getMessage());
        }
        return null;
	}
	
	 /*
	    * �����л�
	    * */

	    public static  <T> T deserialize(byte[] bytes,Class<T> clazz){
	       //�ֽ�����
	        ByteArrayInputStream byteArrayInputStream = null;
	        try{
	            //�� �õ������л��ֽ� ���� ������
	            byteArrayInputStream = new ByteArrayInputStream(bytes);
	            //�����л��� ����������--> ��ʾ�Ŵ� һ�� Դͷ ��ȡ ���� �� ����ȡ �������� ���ֽڣ�
	            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
	            //���򻯳� һ������
	            return (T)objectInputStream.readObject();
	        }catch (Exception e){
	            System.out.println("�����쳣:"+e.getMessage());
	        }
	        return null;
	    }
}
