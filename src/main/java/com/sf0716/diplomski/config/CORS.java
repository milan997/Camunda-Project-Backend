package com.sf0716.diplomski.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@SuppressWarnings("deprecation")
public class CORS extends WebMvcConfigurerAdapter {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowCredentials(true)
			.allowedOrigins("http://localhost:3000", "http://192.168.0.11:3000", "*")
			.allowedMethods("*");
	}
}