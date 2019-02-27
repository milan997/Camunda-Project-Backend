package com.sf0716.diplomski;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Ako ukljucimo @EnableProccessApplication pojavljuje se bag
// 	na hendlerima gde on ne autowireje service te ne mozemo
//  da startujemo process preko mog fronta
// Ako je pak iskljucen onda je contenx path null i ne mozemo dobaviti forme u camundinom frontedu
// @EnableProcessApplication
public class DiplomskiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiplomskiApplication.class, args);
	}
	
}