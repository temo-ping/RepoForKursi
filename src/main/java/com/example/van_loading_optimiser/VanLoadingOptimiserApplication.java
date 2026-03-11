package com.example.van_loading_optimiser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VanLoadingOptimiserApplication {

	public static void main(String[] args) {
		SpringApplication.run(VanLoadingOptimiserApplication.class, args);
	}

	@Bean
	CommandLineRunner printDbProps(
			@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username
	) {
		return args -> {
			System.out.println("DB URL = " + url);
			System.out.println("DB USER = " + username);
		};
	}

}
