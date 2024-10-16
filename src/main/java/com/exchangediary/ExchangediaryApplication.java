package com.exchangediary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ExchangediaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangediaryApplication.class, args);
	}

}
