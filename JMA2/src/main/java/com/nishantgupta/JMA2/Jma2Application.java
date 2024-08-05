package com.nishantgupta.JMA2;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;




@SpringBootApplication
@ComponentScan(basePackages = "com.nishantgupta.JMA2")
public class Jma2Application {

	public static void main(String[] args) {
		SpringApplication.run(Jma2Application.class, args);
	}

}
