package com.marton.theater.billing;

import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marton.theater.domain.Invoice;
import com.marton.theater.domain.Performance;
import com.marton.theater.exceptions.InvalidPerformanceException;

/**
 * Formats invoices into human-readable billing statements.
 */
public class StatementPrinter {

	/**
	 * Generates statements for all invoices (JSON array) and plays (JSON).
	 * 
	 * @param rawInvoices: array of invoice JSON objects
	 * @param rawPlays:    JSON object, mapping playID to play details
	 * @return formatted billing statement
	 */
	public String print(JsonArray rawInvoices, JsonObject rawPlays) throws InvalidPerformanceException {
		Map<String, JsonObject> plays = parsePlays(rawPlays);

		return rawInvoices.asList().stream().map(invoiceJson -> Invoice.fromJson(invoiceJson.getAsJsonObject(), plays))
				.map(this::format).collect(Collectors.joining("\n\n"));
	}

	/**
	 * Converts raw plays (JSON object) into Map.
	 * 
	 * @param rawPlays: JSON object with playID keys
	 * @return Map<String, JsonObject> playID for play data lookup
	 */
	private Map<String, JsonObject> parsePlays(JsonObject rawPlays) {
		return rawPlays.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getAsJsonObject()));
	}

	/**
	 * Formats complete invoice into final billing statement text.
	 * 
	 * @param invoice: domain model invoice, populated
	 * @return formatted statement with header, footer, performance lines, total
	 *         amount, credits etc.
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
