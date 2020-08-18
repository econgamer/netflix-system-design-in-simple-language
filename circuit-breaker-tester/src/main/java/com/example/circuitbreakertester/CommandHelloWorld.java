package com.example.circuitbreakertester;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CommandHelloWorld extends HystrixCommand<String> {
	
	private String name;
	
	CommandHelloWorld(String name){
		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
		this.name = name;
	}
	
	@Override
	protected String run() {
		return "Hello " + name + "!";
	}
	
}
