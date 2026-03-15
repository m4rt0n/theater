package com.marton.theater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marton.theater.models.StatementPrinter;

public class IntegrationTest {
	@Test
	void endToEndBigCoInvoice() throws Exception {
		// GIVEN: Real JSON files
		JsonArray rawInvoices = JsonParser.parseString(Files.readString(Paths.get("invoices.json"))).getAsJsonArray();
		JsonObject rawPlays = JsonParser.parseString(Files.readString(Paths.get("plays.json"))).getAsJsonObject();

		// WHEN: Complete pipeline executes
		StatementPrinter printer = new StatementPrinter();
		String result = printer.print(rawInvoices, rawPlays);

		// THEN: Exact expected output
		String expected = """
				Statement for BigCo
				 Hamlet: $650.00 (55 seats)
				 As You Like It: $580.00 (35 seats)
				 Othello: $500.00 (40 seats)
				Amount owed is $1,730.00
				You earned 47 credits
				""".trim();

		assertEquals(expected.trim(), result.trim(), "Complete pipeline produces exact task output");
	}

	@Test
	void handlesMultipleInvoices() throws Exception {
		// GIVEN: Multiple invoices in array
		JsonArray multiInvoices = createMultiInvoiceJson();
		JsonObject rawPlays = JsonParser.parseString(Files.readString(Paths.get("plays.json"))).getAsJsonObject();

		// WHEN: Complete pipeline executes
		StatementPrinter printer = new StatementPrinter();
		String result = printer.print(multiInvoices, rawPlays);

		// THEN: Processes first invoice only (current behavior)
		assertTrue(result.contains("Statement for BigCo"));
		assertFalse(result.contains("SmallCo")); // Ignores extras
	}

	// Test data factory
	private JsonArray createMultiInvoiceJson() throws Exception {
		String multiJson = """
				[
				  {
				    "customer": "BigCo",
				    "performances": [
				      {"playID": "hamlet", "audience": 55},
				      {"playID": "as-like", "audience": 35}
				    ]
				  },
				  {
				    "customer": "SmallCo",
				    "performances": [
				      {"playID": "othello", "audience": 40}
				    ]
				  }
				]
				""";
		return JsonParser.parseString(multiJson).getAsJsonArray();
	}
}
