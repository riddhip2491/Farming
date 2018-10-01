package com.egr.farming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;


@SpringBootApplication
public class FarmingApplication {

		//https://rest.soilgrids.org/query?lon=5.39&lat=51.57
	public static void main(String[] args) {
		SpringApplication.run(FarmingApplication.class, args);
	}

}
