package com.steam.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EntityScan("com.steam.demo.entity")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ByteApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ByteApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ByteApplication.class);
	}
}