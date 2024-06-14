package com.mediLaboSolutions.frontClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.medicoLaboSolutions.frontClient")
@SpringBootApplication
public class FrontClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontClientApplication.class, args);
	}

}
