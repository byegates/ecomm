package com.wly.ecomm;

import com.wly.ecomm.service.ProductService;
import com.wly.ecomm.service.ShoppingCartService;
import com.wly.ecomm.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@SpringBootApplication
@AllArgsConstructor
public class EcommApplication {
	private final ProductService productService;

	private final UserService userService;

	private final ShoppingCartService shoppingCartService;

	private final JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(EcommApplication.class, args);
	}


	/**
	 * Initialize all tables in database with some initial data
	 * for testers to play with
	 * deals will be initialized inside initProduct method, check it for details
	 */
	@PostConstruct
	private void initDb() throws SQLException {
		Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
		String dbProductName = connection.getMetaData().getDatabaseProductName();
		if (dbProductName.equals("H2"))
			shoppingCartService.initCartItem(userService.initUser(), productService.initProduct());
	}
}
