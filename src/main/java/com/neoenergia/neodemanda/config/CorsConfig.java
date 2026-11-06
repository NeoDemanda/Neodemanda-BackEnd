package com.neoenergia.neodemanda.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	private final String[] origensPermitidas;

	public CorsConfig(@Value("${neodemanda.cors.allowed-origins}") String[] origensPermitidas) {
		if (Arrays.stream(origensPermitidas).anyMatch(origem -> origem.contains("*"))) {
			throw new IllegalArgumentException(
					"NEODEMANDA_CORS_ORIGINS nao pode conter '*': informe as origens permitidas, separadas por virgula");
		}
		this.origensPermitidas = origensPermitidas;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(origensPermitidas)
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
				.allowedHeaders("Content-Type");
	}

}
