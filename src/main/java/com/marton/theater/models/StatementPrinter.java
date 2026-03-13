package com.marton.theater.models;

import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class StatementPrinter {

	public String print(JsonArray rawInvoices, JsonObject rawPlays) {
		// Parse first (and only) invoice from array
		JsonObject rawInvoice = rawInvoices.get(0).getAsJsonObject();
		Map<String, JsonObject> plays = parsePlays(rawPlays);
		Invoice invoice = Invoice.fromJson(rawInvoice, plays);

		return format(invoice);
	}

	private Map<String, JsonObject> parsePlays(JsonObject rawPlays) {
		// Convert plays JSON to Map for easy lookup
		return rawPlays.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getAsJsonObject()));
	}

	private String format(Invoice invoice) {
		StringBuilder result = new StringBuilder();
		result.append(invoice.formatHeader());

		// One line per performance
		for (Performance perf : invoice.performances) {
			result.append(perf.formatLine()).append("\n");
		}

		result.append(invoice.formatFooter());
		result.append(String.format("You earned %d credits\n", invoice.totalCredits()));

		return result.toString();
	}
}
