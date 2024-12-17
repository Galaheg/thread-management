package com.hemre.receiverthread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ReceiverthreadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceiverthreadApplication.class, args);
	}

}
