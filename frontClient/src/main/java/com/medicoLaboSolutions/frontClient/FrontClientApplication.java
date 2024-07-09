package com.medicoLaboSolutions.frontClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients("com.medicoLaboSolutions")
public class FrontClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontClientApplication.class, args);
	}

}
