package com.example.StadiumBooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class StadiumBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StadiumBookingApplication.class, args);
	}

}
