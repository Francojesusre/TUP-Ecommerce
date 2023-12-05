package com.ecommerce.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages="com.ecommerce.app")
@EnableJpaRepositories("com.ecommerce.app.repository")
@EntityScan("com.ecommerce.app.entities")
public class EcomersApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomersApplication.class, args);
	}

}
