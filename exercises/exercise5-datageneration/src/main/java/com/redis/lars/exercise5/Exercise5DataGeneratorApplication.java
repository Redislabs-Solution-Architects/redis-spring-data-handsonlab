package com.redis.lars.exercise5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Exercise5DataGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(Exercise5DataGeneratorApplication.class, args);
	}

}
