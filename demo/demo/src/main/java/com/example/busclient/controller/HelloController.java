package com.example.busclient.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("hello")
public class HelloController {

	@PostMapping("bus/callback")
	public JSONObject callback(@RequestBody JSONObject req) {
		System.out.println("params in ..." + req.toJSONString());
		JSONObject resp = new JSONObject();
		resp.put("code", "0001");
		return resp;
	}
}
