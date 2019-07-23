package com.image.util;

import com.alibaba.fastjson.JSONObject;

public class JsonDemo {
    public static void main(String[] args){
    	JSONObject object = JSONObject.parseObject("");
    	JSONObject data = object.getJSONObject("data");
    	String s = data.getString("redirectUrl");
    	int len = s.length();
    	String token = s.substring(len-32, len);
    	
    	
    }
 
}