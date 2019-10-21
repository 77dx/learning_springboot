package com.flins.test3;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import sun.misc.BASE64Encoder;

public class All_util {

 	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
	public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
	public static final String numberChar = "0123456789"; 
 
	/**
	 * 返回字母和数字随机数
	 * @param length
	 * @return
	 */
	public static String generateString(int length) //参数为返回随机数的长度 
	{ 
		StringBuffer sb = new StringBuffer(); 
		Random random = new Random(); 
		for (int i = 0; i < length; i++) 
		{ 
			sb.append(allChar.charAt(random.nextInt(allChar.length()))); 
		} 
		return sb.toString(); 
	} 
	
	/**
	 * 返回数字随机数
	 * @param length
	 * @return
	 */
	public static String generateNum(int length) 
	{ 
		StringBuffer sb = new StringBuffer(); 
		Random random = new Random(); 
		for (int i = 0; i < length; i++) 
		{ 
			sb.append(numberChar.charAt(random.nextInt(numberChar.length()))); 
		} 
		return sb.toString(); 
	}
		
	//把单张图片转换成base64
	public static String base64(String image_url) {
		InputStream in = null;
		byte[] data = null;
		
		//读取图片字节数组
		try {
			in = new FileInputStream(image_url);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		String base = encoder.encode(data);
		return base;				
	}

}
