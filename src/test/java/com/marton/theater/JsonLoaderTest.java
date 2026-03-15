package com.marton.theater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marton.theater.infrastructure.JsonLoader;

class JsonLoaderTest {

	@Test
	void loadInvoices_handlesJsonArray() throws IOException {
		String jsonArray = "[{\"customer\":\"BigCo\"}]";
		try (Reader reader = new StringReader(jsonArray)) {
			JsonArray result = JsonLoader.loadInvoices(reader);

			assertTrue(result.isJsonArray());
			assertEquals(1, result.size());
		}
	}

	@Test
	void loadInvoices_wrapsSingleObject() throws IOException {
		String jsonObject = "{\"customer\":\"BigCo\"}";
		try (Reader reader = new StringReader(jsonObject)) {
			JsonArray result = JsonLoader.loadInvoices(reader);

			assertTrue(result.isJsonArray());
			assertEquals(1, result.size());
			assertTrue(result.get(0).isJsonObject());
		}
	}

	@Test
	void loadPlays_returnsJsonObject() throws IOException {
		String json = "{\"hamlet\":{\"name\":\"Hamlet\"}}";
		try (Reader reader = new StringReader(json)) {
			JsonObject result = JsonLoader.loadPlays(reader);

			assertTrue(result.isJsonObject());
			assertTrue(result.has("hamlet"));
		}
	}
}
