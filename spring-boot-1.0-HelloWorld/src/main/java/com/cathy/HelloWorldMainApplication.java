package com.cathy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//标注springboot应用
@SpringBootApplication
public class HelloWorldMainApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(HelloWorldMainApplication.class, args);
	}
}
