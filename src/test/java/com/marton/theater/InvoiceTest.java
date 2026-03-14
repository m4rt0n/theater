package com.marton.theater;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.marton.theater.models.Invoice;
import com.marton.theater.models.Performance;

public class InvoiceTest {
	private Map<String, JsonObject> createPlays() {
		JsonObject hamlet = new JsonObject();
		hamlet.addProperty("name", "Hamlet");
		hamlet.addProperty("type", "tragedy");
		JsonObject comedy = new JsonObject();
		comedy.addProperty("name", "As You Like It");
		comedy.addProperty("type", "comedy");
		JsonObject othello = new JsonObject();
		othello.addProperty("name", "Othello");
		othello.addProperty("type", "tragedy");
		return Map.of("hamlet", hamlet, "as-like", comedy, "othello", othello);
	}

	@Test
	void bigCoInvoiceTotals() {
		Map<String, JsonObject> plays = createPlays();
		List<Performance> performances = List.of(new Performance("hamlet", 55, plays),
				new Performance("as-like", 35, plays), new Performance("othello", 40, plays));
		Invoice invoice = new Invoice("BigCo", performances);

		assertEquals(173000, invoice.totalAmount(), "$1,730 total");
		assertEquals(47, invoice.totalCredits(), "47 total credits");
	}

	@Test
	void singlePerformanceInvoice() {
		Map<String, JsonObject> plays = createPlays();
		Invoice invoice = new Invoice("SmallCo", List.of(new Performance("hamlet", 55, plays)));

		assertEquals(65000, invoice.totalAmount());
		assertEquals(25, invoice.totalCredits());
	}

	@Test
	void emptyInvoice() {
		Invoice invoice = new Invoice("EmptyCo", List.of());
		assertEquals(0, invoice.totalAmount());
		assertEquals(0, invoice.totalCredits());
	}
}
