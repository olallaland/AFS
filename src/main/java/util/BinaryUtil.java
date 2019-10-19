package main.java.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BinaryUtil {
	public static String stringToBinary(byte[] b) {
		String result = "";
        for (int i = 0; i < b.length; i++) {
        	String temp = Integer.toBinaryString(b[i] & 0xff);
        	if(temp.length() < 8) {
        		for(int j = 0; j <= 8 - temp.length(); j++) {
        			temp = "0" + temp;
        		}
        	}
        	result += temp;     	
        	System.out.println(temp);
        }
        return result;
	}
	
	public static String binaryToString(String str) throws UnsupportedEncodingException {
		String result;
        // ����������ʽ
        // ƥ��������1��0��ɵ�8λ�ַ�
        Pattern p = Pattern.compile("[10]{8}");

        // ����ƥ��������Դ�ַ���������
        Matcher m = p.matcher(str);

        // ����ƥ����
        //List<Byte> list = new ArrayList<Byte>();  //����һ �÷���  

        List list = new ArrayList(); //������ �������õ�����ʾ����������Object���ʹ洢

        // ��ʼ��Ѱpattern
        // �Ƚ�8λ���ַ�����2����ɨ��ΪInteger
        // ���ں��������������ǿ��ת��byte
        // ����list��
        while (m.find()) {
            list.add((byte) Integer.parseInt(m.group(), 2));
        }

        // ׼����listתΪbyte����
        // ����String�������������͵�����
        byte[] b = new byte[list.size()];

        // ��ʼת��
        for (int j = 0; j < b.length; j++) {
            //b[j] = list.remove(0);   //����һ ȥ�����ͺ�����ᱨ����Ϊȡ������Object����

            b[j] = (Byte) list.remove(0); //������ ��Objectǿ��ת��Byte�Ϳ�����
        }
        /*
         List.remove(int index)�ǽ�ָ��λ�õ�Ԫ��ɾ��,
          Ȼ���ұ�����ʣ�µ�����������һλ�����һ�����ݵĿ�ȱ��
         remove(0)��0��ʾ��һ��Ԫ�أ���ͣ�ĵ���remove(0)��������Ԫ�ر�ɾ�⣬
          ʣ��һ���ռ��ϡ�����ɾ��ָ��Ԫ���⣬ͬʱҲ���з���ֵ�����Ǳ�ɾ����Ԫ�ء�
         */

        // ������ת��ΪString���
        // ���ⲻָ���ַ���(GBK)���ñ�������ϵͳĬ�ϴ�ӡ
        result = new String(b, "GBK");
        System.out.println(result);
        return result;
	}
}
