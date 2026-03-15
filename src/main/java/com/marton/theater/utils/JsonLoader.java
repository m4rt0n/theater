package com.marton.theater.utils;

import java.io.Reader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonLoader {
	/**
	 * Loads invoices file, handles both single object and array formats. Returns
	 * JsonArray ready for StatementPrinter.
	 */
	public static JsonArray loadInvoices(Reader reader) {
		JsonElement root = JsonParser.parseReader(reader);

		if (root.isJsonArray()) {
			return root.getAsJsonArray();
		} else {
			JsonArray array = new JsonArray();
			array.add(root);
			return array;
		}
	}

	/**
	 * Loads plays file as JsonObject.
	 */
	public static JsonObject loadPlays(Reader reader) {
		return JsonParser.parseReader(reader).getAsJsonObject();
	}
}
