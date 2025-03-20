package com.nvurgaft.basketball_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BasketballApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasketballApiApplication.class, args);
	}
}
