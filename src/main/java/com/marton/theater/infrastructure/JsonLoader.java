package com.marton.theater.infrastructure;

import java.io.Reader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Utility class for loading JSON data from Reader streams into Gson data
 * structures.
 */
public class JsonLoader {

	/**
	 * Loads invoices from JSON file, handles both single object and array formats.
	 * 
	 * @param reader: input stream containing JSON data
	 * @return JsonArray containing one or more invoice objects
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
	 * Loads plays from JSON file.
	 * 
	 * @param reader: input stream containing plays JSON object
	 * @return JsonObject mapping playID to play definition
	 */
	public static JsonObject loadPlays(Reader reader) {
		return JsonParser.parseReader(reader).getAsJsonObject();
	}
}
