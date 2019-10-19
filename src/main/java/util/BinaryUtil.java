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
        // 定义正则表达式
        // 匹配所有由1或0组成的8位字符
        Pattern p = Pattern.compile("[10]{8}");

        // 定义匹配器，与源字符串关连上
        Matcher m = p.matcher(str);

        // 安放匹配结果
        //List<Byte> list = new ArrayList<Byte>();  //方法一 用泛型  

        List list = new ArrayList(); //方法二 将泛型拿掉，表示所有数据以Object类型存储

        // 开始搜寻pattern
        // 先将8位的字符串按2进制扫描为Integer
        // 由于后面的须求数字再强制转成byte
        // 存入list中
        while (m.find()) {
            list.add((byte) Integer.parseInt(m.group(), 2));
        }

        // 准备将list转为byte数组
        // 由于String构造器参数类型的限制
        byte[] b = new byte[list.size()];

        // 开始转换
        for (int j = 0; j < b.length; j++) {
            //b[j] = list.remove(0);   //方法一 去掉泛型后这里会报错，因为取出的是Object类型

            b[j] = (Byte) list.remove(0); //方法二 把Object强制转成Byte就可以了
        }
        /*
         List.remove(int index)是将指定位置的元素删除,
          然后右边所有剩下的数据向左移一位，填补第一个数据的空缺。
         remove(0)中0表示第一个元素，不停的调用remove(0)导致所有元素被删光，
          剩下一个空集合。除了删除指定元素外，同时也具有返回值，就是被删掉的元素。
         */

        // 将数组转换为String输出
        // 故意不指定字符集(GBK)，让编绎器按系统默认打印
        result = new String(b, "GBK");
        System.out.println(result);
        return result;
	}
}
