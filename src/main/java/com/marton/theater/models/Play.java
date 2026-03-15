package com.marton.theater.models;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPlayDataException;
import com.marton.theater.exceptions.InvalidPlayTypeException;

/**
 * Abstract base for theatrical plays. Defines pricing contract.
 */
public abstract class Play {
	public final String name;

	protected Play(String name) {
		this.name = name;
	}

	/**
	 * Factory method creates Play instance from raw JSON play data.
	 * 
	 * @param playID   unique identifier ("hamlet", "as-like")
	 * @param playData JSON object containing "name" and "type" fields
	 * @return concrete Play subclass (Tragedy or Comedy)
	 * @throws InvalidPlayDataException if JSON malformed or type unknown
	 */
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

	/**
	 * Returns the charge for this play given the audience size (in cents).
	 * 
	 * <p>
	 * Tragedies charge $400 base + $10 per seat over 30. Comedies charge $300 base
	 * + $100 flat + $5 per seat over 20 + $3 per seat always.
	 * 
	 * @param audience number of seats sold (0-100 typical)
	 * @return total charge in cents
	 */
	public abstract int calculateAmount(int audience);

	/**
	 * Calculates volume credits for this play given audience size.
	 * 
	 * <p>
	 * Standard volume credits: 1 credit per seat over 30. Comedies receive bonus: 1
	 * extra credit per 5 seats sold.
	 * 
	 * @param audience number of seats sold
	 * @return total loyalty credits earned
	 */
	public abstract int calculateVolumeCredits(int audience);

	public String formatAmount(int cents) {
		return dollars(cents);
	}

	public static String dollars(int cents) {
		int dollars = cents / 100;
		return String.format("$%,d.00", dollars);
	}
}
