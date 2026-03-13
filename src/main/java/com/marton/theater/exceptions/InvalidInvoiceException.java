package com.marton.theater.exceptions;

public class InvalidInvoiceException extends TheaterException {
	public InvalidInvoiceException(String message) {
		super("Invalid invoice: " + message);
	}
}
