package com.co.solia.emotional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * the entry point of solia monolithic.
 *
 * @author luis.bolivar
 */
@SpringBootApplication()
@EnableMongoRepositories
public class EmotionalApplication {

	/**
	 * entry point to solia spring call.
	 * @param args from console run.
	 */
	public static void main(String[] args) {
		SpringApplication.run(EmotionalApplication.class, args);
	}

}
