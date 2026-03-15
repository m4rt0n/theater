package com.marton.theater.domain;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPlayDataException;
import com.marton.theater.exceptions.InvalidPlayTypeException;

/**
 * Abstract base class for theatrical plays. Defines pricing (amount) and
 * loyalty points (volume credits) calculation. Contains amount formatter
 * (cents-to-dollars)
 */
public abstract class Play {
	public final String name;

	protected Play(String name) {
		this.name = name;
	}

	/**
	 * Factory method, creates Play instance from raw JSON play data.
	 * 
	 * @param playID:   unique identifier e.g. ("hamlet", "as-like")
	 * @param playData: JSON object, contains "name" and "type" fields
	 * @return Play subclass (Tragedy or Comedy)
	 * @throws InvalidPlayDataException (if JSON malformed or type unknown)
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
	 * Calculates harge for this play by audience size (in cents).
	 * 
	 * <p>
	 * Tragedies charge $400 base + $10 per seat over 30. Comedies charge $300 base
	 * + $100 flat + $5 per seat over 20 + $3 per seat always.
	 * 
	 * @param audience: number of seats sold
	 * @return total charge in cents
	 */
	public abstract int calculateAmount(int audience);

	/**
	 * Calculates volume credits for this play by audience size.
	 * 
	 * <p>
	 * Standard volume credits: 1 credit per seat over 30. Comedy bonus: 1 extra
	 * credit per 5 seats sold.
	 * 
	 * @param audience: number of seats sold
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
