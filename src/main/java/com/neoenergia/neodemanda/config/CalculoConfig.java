package com.neoenergia.neodemanda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.neoenergia.neodemanda.motor.CalculadoraDemanda;

@Configuration
public class CalculoConfig {

	@Bean
	public CalculadoraDemanda calculadoraDemanda() {
		return new CalculadoraDemanda();
	}

}
