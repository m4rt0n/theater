package com.marton.theater.models;

import java.util.Map;

import com.google.gson.JsonObject;

public class Performance {

	public final Play play;
	public final int audience;

	public Performance(String playID, int audience, Map<String, JsonObject> rawPlays) {

		this.audience = audience;
		this.play = Play.createFromJson(playID, rawPlays.get(playID));

	}

	public int amount() {
		return play.amount(audience);
	}

	public int credits() {
		return play.credits(audience);
	}

	public String formatLine() {
		return String.format(" %s: %s (%d seats)", play.name, play.formatAmount(amount()), audience);
	}
}
