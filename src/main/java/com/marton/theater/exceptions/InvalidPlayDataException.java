package com.marton.theater.exceptions;

public class InvalidPlayDataException extends TheaterException {
	public InvalidPlayDataException(String playID, String reason) {
		super("Invalid play data for '" + playID + "': " + reason);
	}
}
