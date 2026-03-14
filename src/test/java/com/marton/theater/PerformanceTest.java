package com.marton.theater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Map;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPerformanceException;
import com.marton.theater.models.Performance;

public class PerformanceTest {
	private Map<String, JsonObject> createPlays() {
		JsonObject hamlet = new JsonObject();
		hamlet.addProperty("name", "Hamlet");
		hamlet.addProperty("type", "tragedy");
		return Map.of("hamlet", hamlet);
	}

	@Test
	void delegatesAmountToPlay() {
		Map<String, JsonObject> plays = createPlays();
		Performance perf = new Performance("hamlet", 55, plays);
		assertEquals(65000, perf.amount(), "Delegates to TragedyPlay");
	}

	@Test
	void delegatesCreditsToPlay() {
		Map<String, JsonObject> plays = createPlays();
		Performance perf = new Performance("hamlet", 55, plays);
		assertEquals(25, perf.credits(), "Delegates to TragedyPlay");
	}

	@Test
	void formatsPerformanceLine() {
		Map<String, JsonObject> plays = createPlays();
		Performance perf = new Performance("hamlet", 55, plays);
		assertEquals(" Hamlet: $650.00 (55 seats)", perf.formatLine());
	}

	@Test
	void rejectsNullPlayID() {
		Map<String, JsonObject> plays = createPlays();
		assertThrows(InvalidPerformanceException.class, () -> new Performance(null, 55, plays));
	}

	@Test
	void rejectsEmptyPlayID() {
		Map<String, JsonObject> plays = createPlays();
		assertThrows(InvalidPerformanceException.class, () -> new Performance("", 55, plays));
	}

	@Test
	void rejectsNegativeAudience() {
		Map<String, JsonObject> plays = createPlays();
		assertThrows(InvalidPerformanceException.class, () -> new Performance("hamlet", -1, plays));
	}

	@Test
	void rejectsUnknownPlayID() {
		Map<String, JsonObject> plays = Map.of();
		assertThrows(InvalidPerformanceException.class, () -> new Performance("unknown", 55, plays));
	}
}
