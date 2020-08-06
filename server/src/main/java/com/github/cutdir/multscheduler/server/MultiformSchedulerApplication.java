package com.github.cutdir.multscheduler.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author cutdir
 */
@SpringBootApplication
@EnableAutoConfiguration
public class MultiformSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiformSchedulerApplication.class, args);
	}
}
