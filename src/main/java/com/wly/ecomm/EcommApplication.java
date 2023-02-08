package com.wly.ecomm;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class EcommApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommApplication.class, args);
	}

}
