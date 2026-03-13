package com.marton.theater.models;

import java.util.Map;

import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPerformanceException;
import com.marton.theater.exceptions.InvalidPlayDataException;

public class Performance {

	public final Play play;
	public final int audience;
	public final String playID;

	public Performance(String playID, int audience, Map<String, JsonObject> rawPlays)
			throws InvalidPerformanceException, InvalidPlayDataException {
		if (playID == null || playID.trim().isEmpty()) {
			throw new InvalidPerformanceException("playID is required");
		}
		if (audience < 0) {
			throw new InvalidPerformanceException("audience cannot be negative: " + audience);
		}
		if (!rawPlays.containsKey(playID)) {
			throw new InvalidPerformanceException("Unknown playID: " + playID);
		}

		this.playID = playID; // ← NOW COMPILER HAPPY
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
