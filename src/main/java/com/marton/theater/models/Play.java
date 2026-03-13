package com.marton.theater.models;

import java.util.Map;

import com.google.gson.JsonObject;

public abstract class Play {
	public final String name;

	public Play(String name) {
		this.name = name;
	}

	// factory method - separate factory class needed?
	public static Play createFromJson(String playID, JsonObject playData, Map<String, Play> cache) {
		if (cache.containsKey(playID)) {
			return cache.get(playID);
		}

		String type = playData.get("type").getAsString();
		String name = playData.get("name").getAsString();

		Play play;
		switch (type) {
		case "tragedy":
			play = new Tragedy(name);
			break;
		case "comedy":
			play = new Comedy(name);
			break;
		default:
			throw new IllegalArgumentException("Unknown play type: " + type);
		}

		cache.put(playID, play);
		return play;
	}

	public abstract int amount(int audience);

	public abstract int credits(int audience);

	public String formatAmount(int amount) {
		return String.format("$%d.00", amount / 100);
	}
}
