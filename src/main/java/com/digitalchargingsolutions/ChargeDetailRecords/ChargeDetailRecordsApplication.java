package com.digitalchargingsolutions.ChargeDetailRecords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ChargeDetailRecordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChargeDetailRecordsApplication.class, args);
	}

}
