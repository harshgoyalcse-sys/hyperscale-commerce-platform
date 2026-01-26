package com.hscp.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UserServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
