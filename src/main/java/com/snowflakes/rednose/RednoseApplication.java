package com.snowflakes.rednose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RednoseApplication {

	public static void main(String[] args) {
		SpringApplication.run(RednoseApplication.class, args);
	}

}
