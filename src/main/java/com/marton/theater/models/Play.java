package com.marton.theater.models;

import com.google.gson.JsonObject;

public abstract class Play {
	public final String name;

	public Play(String name) {
		this.name = name;
	}

	// factory method - separate factory class needed?
	// Violates Single Responsibility (Play now knows about JSON parsing)
	public static Play createFromJson(String playID, JsonObject playData) {

		if (playData == null) {
			throw new IllegalArgumentException("Play data required for " + playID);
		}

		String type = playData.get("type").getAsString();
		String name = playData.get("name").getAsString();

		switch (type) {
		case "tragedy":
			return new Tragedy(name);
		case "comedy":
			return new Comedy(name);
		default:
			throw new IllegalArgumentException("Unknown play type: " + type);
		}
	}

	public abstract int amount(int audience);

	public abstract int credits(int audience);

	public String formatAmount(int amount) {
		return String.format("$%d.00", amount / 100);
	}
}
