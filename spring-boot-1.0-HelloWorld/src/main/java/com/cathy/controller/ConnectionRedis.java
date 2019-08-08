package com.cathy.controller;

import redis.clients.jedis.Jedis;

public class ConnectionRedis {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("148.70.170.189",6379);
		jedis.set("name","kathy");
		
		String name = jedis.get("name");
		System.out.println(name);
		jedis.close();

	}

}
