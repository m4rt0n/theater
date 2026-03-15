package com.marton.theater;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marton.theater.domain.Invoice;

class InvoiceTest {
	private Map<String, JsonObject> plays;

	@BeforeEach
	void setUp() {
		plays = Map.of("hamlet", createPlay("Hamlet", "tragedy"), // ← Define this method
				"as-like", createPlay("As You Like It", "comedy"));
	}

	// ✅ ADD THIS METHOD
	private JsonObject createPlay(String name, String type) {
		JsonObject play = new JsonObject();
		play.addProperty("name", name);
		play.addProperty("type", type);
		return play;
	}

	// Also need createBigCoInvoice()
	private JsonObject createBigCoInvoice() {
		JsonObject invoice = new JsonObject();
		invoice.addProperty("customer", "BigCo");
		JsonArray performances = new JsonArray();
		performances.add(createPerformance("hamlet", 55));
		invoice.add("performances", performances);
		return invoice;
	}

	private JsonObject createPerformance(String playID, int audience) {
		JsonObject perf = new JsonObject();
		perf.addProperty("playID", playID);
		perf.addProperty("audience", audience);
		return perf;
	}

	@Test
	void fromJson_createsValidInvoice() {
		JsonObject raw = createBigCoInvoice();
		Invoice invoice = Invoice.fromJson(raw, plays);
		assertEquals("BigCo", invoice.customer);
	}
}
