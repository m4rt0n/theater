package com.marton.theater.exceptions;

public class InvalidPlayTypeException extends TheaterException {
	public InvalidPlayTypeException(String type) {
		super("Unknown play type: " + type);
	}
}
