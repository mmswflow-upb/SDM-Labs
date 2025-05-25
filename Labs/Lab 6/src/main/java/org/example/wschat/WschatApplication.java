package org.example.wschat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WschatApplication {
	public static void main(String[] args) {
		SpringApplication.run(WschatApplication.class, args);
	}}
