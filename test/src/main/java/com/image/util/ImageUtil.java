package com.image.util;
import java.io.*;
import java.util.ArrayList;

import sun.misc.BASE64Encoder;

public class ImageUtil {
	
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
	
	//返回该路径下的所有文件路径集合
	public static ArrayList<String> getImageUrl(String file_url) {
		ArrayList<String> image_list = new ArrayList<String>();
		File files = new File(file_url);
		File[] list = files.listFiles();
		for (File file : list) {
			image_list.add(file.getPath());
		}
		return image_list;
	}
	
	

	
	public static void main(String[] args) {
//		String base = ImageUtil.base64("D:/images/TR6.jpg");
//		System.out.println(base);
		
	}
}
