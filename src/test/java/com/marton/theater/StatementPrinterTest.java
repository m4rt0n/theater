package com.marton.theater;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marton.theater.models.StatementPrinter;

public class StatementPrinterTest {
	@Test
	void printsBigCoStatement() throws Exception {
		StatementPrinter printer = new StatementPrinter();
		JsonArray invoices = JsonParser.parseString(Files.readString(Paths.get("invoices.json"))).getAsJsonArray();
		JsonObject plays = JsonParser.parseString(Files.readString(Paths.get("plays.json"))).getAsJsonObject();

		String result = printer.print(invoices, plays);

		assertTrue(result.contains("Statement for BigCo"));
		assertTrue(result.contains("Hamlet: $650.00 (55 seats)"));
		assertTrue(result.contains("As You Like It: $580.00 (35 seats)"));
		assertTrue(result.contains("Othello: $500.00 (40 seats)"));
		assertTrue(result.contains("Amount owed is $1,730.00"));
		assertTrue(result.contains("You earned 47 credits"));
	}
}
