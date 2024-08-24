package com.steam.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@EntityScan("com.steam.demo.entity")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ByteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ByteApplication.class, args);
	}
}
