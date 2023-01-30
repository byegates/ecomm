package com.wly.ecomm;

import com.wly.ecomm.service.ProductService;
import com.wly.ecomm.service.ShoppingCartService;
import com.wly.ecomm.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class EcommApplication {
	private final ProductService productService;

	private final UserService userService;

	private final ShoppingCartService shoppingCartService;

	public static void main(String[] args) {
		SpringApplication.run(EcommApplication.class, args);
	}


	/**
	 * Initialize all tables in database with some initial data
	 * for testers to play with
	 * deals will be initialized inside initProduct method, check it for details
	 */
	@PostConstruct
	private void initDb() {
		var products = productService.initProduct();
		var customers = userService.initUser();
		shoppingCartService.initCartItem(customers, products);
	}
}
