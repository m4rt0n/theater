package com.marton.theater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marton.theater.models.StatementPrinter;

public class IntegrationTest {
	@Test
	void endToEndBigCoInvoice() throws Exception {
		// GIVEN: Real JSON files from kata
		JsonArray rawInvoices = JsonParser.parseString(Files.readString(Paths.get("invoices.json"))).getAsJsonArray();
		JsonObject rawPlays = JsonParser.parseString(Files.readString(Paths.get("plays.json"))).getAsJsonObject();

		// WHEN: Complete pipeline executes
		StatementPrinter printer = new StatementPrinter();
		String result = printer.print(rawInvoices, rawPlays);

		// THEN: Exact expected output from kata
		String expected = """
				Statement for BigCo
				    Hamlet: $650.00 (55 seats)
				    As You Like It: $580.00 (35 seats)
				    Othello: $500.00 (40 seats)
				  Amount owed is $1,730.00
				  You earned 47 credits
				""";

		assertEquals(expected.trim(), result.trim(), "Complete pipeline produces exact kata output");
	}

	@Test
	void handlesMultipleInvoices() throws Exception {
		// GIVEN: Multiple invoices in array (extension of kata)
		JsonArray multiInvoices = createMultiInvoiceJson();
		JsonObject rawPlays = JsonParser.parseString(Files.readString(Paths.get("plays.json"))).getAsJsonObject();

		// WHEN:
		StatementPrinter printer = new StatementPrinter();
		String result = printer.print(multiInvoices, rawPlays);

		// THEN: Processes first invoice only (current behavior)
		assertTrue(result.contains("Statement for BigCo"));
		assertFalse(result.contains("SecondCo")); // Ignores extras
	}

	@Test
	void mainMethodEndToEnd() throws Exception {
		// GIVEN: Run main() as black box
		TheaterMainApp main = new TheaterMainApp();
		// Capture System.out output (requires ByteArrayOutputStream setup)

		// WHEN: Main executes with real files
		// main.main(new String[0]);

		// THEN: Matches StatementPrinter output
		// assertOutputMatchesExpected();
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
				    "customer": "SecondCo",
				    "performances": [
				      {"playID": "othello", "audience": 40}
				    ]
				  }
				]
				""";
		return JsonParser.parseString(multiJson).getAsJsonArray();
	}
}
