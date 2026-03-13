package com.marton.theater.models;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

public class Invoice {
	public final String customer;
	public final List<Performance> performances;

	public Invoice(String customer, List<Performance> performances) {
		this.customer = customer;
		this.performances = List.copyOf(performances); // Defensive copy
	}

	// STATIC FACTORY METHOD - creates Invoice from raw JSON
	public static Invoice fromJson(JsonObject rawInvoice, Map<String, JsonObject> rawPlays) {
		String customer = rawInvoice.get("customer").getAsString();

		List<Performance> performances = rawInvoice.getAsJsonArray("performances").asList().stream().map(rawPerf -> {
			JsonObject perfData = rawPerf.getAsJsonObject();
			String playID = perfData.get("playID").getAsString();
			int audience = perfData.get("audience").getAsInt();
			return new Performance(playID, audience, rawPlays); // Uses Play.fromJson()
		}).collect(Collectors.toList());

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
