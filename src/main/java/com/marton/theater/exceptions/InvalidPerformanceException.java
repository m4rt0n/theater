package com.marton.theater.exceptions;

public class InvalidPerformanceException extends TheaterException {
	public InvalidPerformanceException(String message) {
		super("Invalid performance: " + message);
	}
}