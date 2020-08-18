package com.example.circuitbreakertester;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CircuitBreakerTesterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerTesterApplication.class, args);
	}
	
	@Bean
	public ServletRegistrationBean adminServletRegistrationBean() {
		return new ServletRegistrationBean(new HystrixMetricsStreamServlet(), "/hystrix.stream");
	}

}
