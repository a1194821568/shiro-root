package com.help.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/")
	String home(){
		return "hello world3453453454323123413251345234";
	}
	
	
	@GetMapping("/value")
	String home2(){
		return "-------------------hello world3453453454323123413251345234";
	}
}
