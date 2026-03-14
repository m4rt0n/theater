package com.marton.theater.models;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPlayDataException;
import com.marton.theater.exceptions.InvalidPlayTypeException;

public abstract class Play {
	public final String name;

	protected Play(String name) {
		this.name = name;
	}

	// factory method - separate factory class needed?
	// Violates Single Responsibility (Play now knows about JSON parsing)
	// type exists only at JSON parsing time (factory).
	// After that, inheritance makes it redundant
	public static Play createFromJson(String playID, JsonObject playData) throws InvalidPlayDataException {
		if (playData == null) {
			throw new InvalidPlayDataException(playID, "Play data is null");
		}

		return Optional.ofNullable(playData.get("type")).map(JsonElement::getAsString).map(type -> switch (type) {
		case "tragedy" -> new Tragedy(playData.get("name").getAsString());
		case "comedy" -> new Comedy(playData.get("name").getAsString());
		default -> throw new InvalidPlayTypeException(type);
		}).orElseThrow(() -> new InvalidPlayDataException(playID, "Missing 'type' field"));
	}

	public abstract int amount(int audience);

	public abstract int credits(int audience);

	public String formatAmount(int amount) {
		return String.format("$%d.00", amount / 100);
	}
}
