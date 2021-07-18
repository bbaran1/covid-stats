package com.bbaran1.covidstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CovidStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidStatsApplication.class, args);
	}

}
