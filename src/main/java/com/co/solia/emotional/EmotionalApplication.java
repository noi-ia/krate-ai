package com.co.solia.emotional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * the entry point of solia monolithic.
 *
 * @author luis.bolivar
 */
@SpringBootApplication()
public class EmotionalApplication {

	/**
	 * entry point to solia spring call.
	 * @param args from console run.
	 */
	public static void main(String[] args) {
		SpringApplication.run(EmotionalApplication.class, args);
	}

}
