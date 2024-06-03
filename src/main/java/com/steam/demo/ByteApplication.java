package com.steam.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan("com.steam.entity")
@EnableTransactionManagement
public class ByteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ByteApplication.class, args);
	}

}
