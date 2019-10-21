package com.example.busclient.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("hello")
public class HelloController {

	@PostMapping("bus/callback")
	public JSONObject callback(@RequestBody JSONObject req) {
		System.out.println("params in ..." + req.toJSONString());
		JSONObject resp = new JSONObject();
//		resp.put("code", "0000");
		resp.put("BText","甲状腺测评报告");
		resp.put("state", "success");
		resp.put("prediction", "6");
		return resp;
	}
	
	@PostMapping("bus/users")
	public JSONObject users() {
		OperateMysql o = new OperateMysql();
		List<String> users = new ArrayList<String>();
		String para =null;
		ArrayList<User> list = o.select();
		for (User user : list) {
			para = "{\r\n" + 
					"        \"id\":\""+user.getId()+"\",\r\n" + 
					"        \"account\":\""+user.getAccount()+"\",\r\n" + 
					"        \"pwd\":\""+user.getPwd()+"\",\r\n" + 
					"        \"name\":\""+user.getName()+"\",\r\n" + 
					"        \"email\":\""+user.getEmail()+"\",\r\n" + 
					"        \"phone\":\""+user.getPhone()+"\",\r\n" + 
					"        \"wechat_open_id\":\""+user.getWechat_open_id()+"\",\r\n" + 
					"        \"last_login_time\":\""+user.getLast_login_time()+"\",\r\n" + 
					"        \"status\":\""+user.getStatus()+"\",\r\n" + 
					"        \"create_time\":\""+user.getCreate_time()+"\",\r\n" + 
					"    }";
			users.add(para);
		}
		o.close();
		JSONObject d =null;
		ArrayList data = new ArrayList<>();
		for (String user : users) {
			d = JSON.parseObject(user);
			data.add(d);
			
		}
		
		JSONObject resp = new JSONObject();
		resp.put("code", "0000");
		resp.put("desc", "请求成功");
		resp.put("data", data);
		System.out.println(resp.toJSONString());
		return resp;
	}
}
