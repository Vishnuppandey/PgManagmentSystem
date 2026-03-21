package com.pg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PgManagmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PgManagmentApplication.class, args);
	}

}
