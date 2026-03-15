package com.marton.theater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marton.theater.models.StatementPrinter;

class StatementPrinterTest {

	private StatementPrinter printer;

	@BeforeEach
	void setUp() {
		printer = new StatementPrinter();
	}

	@Test
	void processesMultipleInvoices() {
		JsonArray invoices = createMultipleInvoices();

		JsonObject rawPlays = createPlaysJson();
		String result = printer.print(invoices, rawPlays);

		assertTrue(result.contains("Statement for BigCo"));
		assertTrue(result.contains("Statement for SecondCo"));
	}

	@Test
	void handlesSingleInvoice() {
		JsonArray single = new JsonArray();
		single.add(createBigCoInvoice());
		JsonObject rawPlays = createPlaysJson();

		String result = printer.print(single, rawPlays);

		assertTrue(result.contains("Statement for BigCo"));
		assertEquals(1, result.split("\n\n").length);
	}

	private JsonObject createPlaysJson() {
		JsonObject plays = new JsonObject();
		plays.add("hamlet", createPlay("Hamlet", "tragedy"));
		plays.add("as-like", createPlay("As You Like It", "comedy"));
		plays.add("othello", createPlay("Othello", "tragedy"));
		return plays;
	}

	private JsonObject createPlay(String name, String type) {
		JsonObject play = new JsonObject();
		play.addProperty("name", name);
		play.addProperty("type", type);
		return play;
	}

	private JsonArray createMultipleInvoices() {
		JsonArray invoices = new JsonArray();
		invoices.add(createBigCoInvoice());
		invoices.add(createSecondCoInvoice());
		return invoices;
	}

	private JsonObject createBigCoInvoice() {
		JsonObject invoice = new JsonObject();
		invoice.addProperty("customer", "BigCo");
		JsonArray performances = new JsonArray();
		performances.add(createPerformance("hamlet", 55));
		performances.add(createPerformance("as-like", 35));
		performances.add(createPerformance("othello", 40));
		invoice.add("performances", performances);
		return invoice;
	}

	private JsonObject createSecondCoInvoice() {
		JsonObject invoice = new JsonObject();
		invoice.addProperty("customer", "SecondCo");
		JsonArray performances = new JsonArray();
		performances.add(createPerformance("as-like", 35));
		invoice.add("performances", performances);
		return invoice;
	}

	private JsonObject createPerformance(String playID, int audience) {
		JsonObject perf = new JsonObject();
		perf.addProperty("playID", playID);
		perf.addProperty("audience", audience);
		return perf;
	}

}