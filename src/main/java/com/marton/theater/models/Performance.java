package com.marton.theater.models;

import java.util.Map;

import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPerformanceException;
import com.marton.theater.exceptions.InvalidPlayDataException;

/**
 * Single play performance with audience count and resolved Play reference.
 */
public class Performance {

	public final Play play;
	public final int audience;

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

		this.audience = audience;
		this.play = Play.createFromJson(playID, rawPlays.get(playID));
	}

	/**
	 * Returns the total charge for this performance (delegates to Play).
	 * 
	 * @return charge in cents
	 */
	public int amount() {
		return play.amount(audience);
	}

	/**
	 * Returns volume credits for this performance (delegates to Play).
	 * 
	 * @return total loyalty credits
	 */
	public int credits() {
		return play.credits(audience);
	}

	/**
	 * Returns total amount owed across all performances in this invoice.
	 * 
	 * @return total charge in cents
	 */
	public String formatLine() {
		return String.format(" %s: %s (%d seats)", play.name, play.formatAmount(amount()), audience);
	}
}
