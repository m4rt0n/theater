package com.marton.theater.models;

import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPerformanceException;

/**
 * Formats invoices into human-readable statements.
 */
public class StatementPrinter {

	/**
	 * Generates statements for ALL invoices in the JSON array and JSON plays.
	 * 
	 * @param rawInvoices array of invoice JSON objects
	 * @param rawPlays    JSON object mapping playID to play details
	 * @return complete formatted statement text
	 */
	public String print(JsonArray rawInvoices, JsonObject rawPlays) throws InvalidPerformanceException {
		Map<String, JsonObject> plays = parsePlays(rawPlays);

		return rawInvoices.asList().stream()
				.map(invoiceJson -> Invoice.createFromJson(invoiceJson.getAsJsonObject(), plays)).map(this::format)
				.collect(Collectors.joining("\n\n"));
	}

	/**
	 * Normalizes raw plays JSON object into Map for O(1) lookup.
	 * 
	 * @param rawPlays JSON object with playID keys
	 * @return Map<String, JsonObject> for playID → play data lookup
	 */
	private Map<String, JsonObject> parsePlays(JsonObject rawPlays) {
		// Convert plays JSON to Map for easy lookup
		return rawPlays.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getAsJsonObject()));
	}

	/**
	 * Formats complete invoice into final statement text.
	 * 
	 * @param invoice fully-populated domain model invoice
	 * @return formatted statement with header, performance lines, totals, credits
	 */
	private String format(Invoice invoice) {
		StringBuilder result = new StringBuilder();
		result.append(invoice.formatHeader());

		for (Performance perf : invoice.performances) {
			result.append(perf.formatLine()).append("\n");
		}

		result.append(invoice.formatFooter());
		result.append(String.format("You earned %d credits\n", invoice.totalCredits()));

		return result.toString();
	}
}
