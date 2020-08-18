package com.example.routingandfilteringbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RoutingAndFilteringBookApplication {
	
	private static Logger log = LoggerFactory.getLogger(SpringBootApplication.class);

	@RequestMapping(value = "/available")
	public String available() {
		log.info("Spring in Action");
		return "Spring in Action";
	}
	
	@RequestMapping(value = "/checked-out")
	public String checkedOut() {
		log.info("checked-out");
		return "Spring Boot in Action";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RoutingAndFilteringBookApplication.class, args);
	}

}
