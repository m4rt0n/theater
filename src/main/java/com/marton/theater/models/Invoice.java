package com.marton.theater.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPerformanceException;
import com.marton.theater.exceptions.InvalidPlayDataException;

public class Invoice {
	public final String customer;
	public final List<Performance> performances;

	public Invoice(String customer, List<Performance> performances) {
		this.customer = customer;
		this.performances = List.copyOf(performances); // Defensive copy
	}

	// STATIC FACTORY METHOD - creates Invoice from raw JSON
	public static Invoice createFromJson(JsonObject rawInvoice, Map<String, JsonObject> rawPlays)
			throws InvalidPerformanceException, InvalidPlayDataException {
		String customer = rawInvoice.get("customer").getAsString();
		JsonArray performancesArray = rawInvoice.getAsJsonArray("performances");

		List<Performance> performances = new ArrayList<>();
		for (JsonElement element : performancesArray) {
			JsonObject perfData = element.getAsJsonObject();
			String playID = perfData.get("playID").getAsString();
			int audience = perfData.get("audience").getAsInt();
			performances.add(new Performance(playID, audience, rawPlays));
		}

		return new Invoice(customer, performances);
	}

	// Total amount owed across all performances
	public int totalAmount() {
		return performances.stream().mapToInt(Performance::amount).sum();
	}

	// Total volume credits across all performances
	public int totalCredits() {
		return performances.stream().mapToInt(Performance::credits).sum();
	}

	// Convenience for statement formatting
	public String formatHeader() {
		return "Statement for " + customer + "\n";
	}

	public String formatFooter() {
		return String.format("Amount owed is $%d.00\n", totalAmount() / 100);
	}
}
