package com.neoenergia.neodemanda.exception;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiError(
		OffsetDateTime timestamp,
		int status,
		String error,
		String message,
		String path,
		Map<String, String> fields) {

	public static ApiError of(int status, String error, String message, String path) {
		return new ApiError(OffsetDateTime.now(), status, error, message, path, Map.of());
	}

	public static ApiError ofValidation(int status, String error, String message, String path,
			Map<String, String> fields) {
		return new ApiError(OffsetDateTime.now(), status, error, message, path, Map.copyOf(fields));
	}

}
