package com.clickbus.placesapi.config;

import java.time.Clock;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper createModelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public Clock createClock() {
		return Clock.systemDefaultZone();
	}
	
}
