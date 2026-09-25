package com.marcos.aulanote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AulanoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(AulanoteApplication.class, args);
	}

}
