package com.neoenergia.neodemanda.service;

import org.springframework.stereotype.Service;

import com.neoenergia.neodemanda.dto.HealthResponse;


@Service
public class HealthService {

	private static final String STATUS_OK = "ok";

	
	public HealthResponse check() {
		return new HealthResponse(STATUS_OK);
	}

}
