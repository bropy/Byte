package com.steam.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EntityScan("com.steam.entity")
@EnableTransactionManagement
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class ByteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ByteApplication.class, args);
	}
}