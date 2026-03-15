package com.marton.theater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPlayDataException;
import com.marton.theater.exceptions.InvalidPlayTypeException;
import com.marton.theater.models.Comedy;
import com.marton.theater.models.Play;
import com.marton.theater.models.Tragedy;

class PlayTest {

	@Test
	void createFromJson_tragedy() {
		// Arrange
		JsonObject playData = createPlayJson("Hamlet", "tragedy");

		// Act
		Play hamlet = Play.createFromJson("hamlet", playData);

		// Assert
		assertEquals("Hamlet", hamlet.name);
		assertInstanceOf(Tragedy.class, hamlet);
	}

	@Test
	void createFromJson_comedy() {
		JsonObject playData = createPlayJson("As You Like It", "comedy");
		Play comedy = Play.createFromJson("as-like", playData);

		assertEquals("As You Like It", comedy.name);
		assertInstanceOf(Comedy.class, comedy);
	}

	@Test
	void createFromJson_nullData_throwsInvalidPlayDataException() {
		InvalidPlayDataException exception = assertThrows(InvalidPlayDataException.class,
				() -> Play.createFromJson("hamlet", null));
		assertEquals("Invalid play data for 'hamlet': Play data is null", exception.getMessage());
	}

	@Test
	void createFromJson_missingType_throwsInvalidPlayDataException() {
		JsonObject noType = new JsonObject();
		noType.addProperty("name", "Hamlet");

		InvalidPlayDataException exception = assertThrows(InvalidPlayDataException.class,
				() -> Play.createFromJson("hamlet", noType));
		assertEquals("Invalid play data for 'hamlet': Missing 'type' field", exception.getMessage());
	}

	@Test
	void createFromJson_invalidType_throwsInvalidPlayTypeException() {
		JsonObject invalidType = createPlayJson("Invalid", "musical");

		InvalidPlayTypeException exception = assertThrows(InvalidPlayTypeException.class,
				() -> Play.createFromJson("invalid", invalidType));
		assertEquals("Unknown play type: musical", exception.getMessage());
	}

	@Test
	void tragedy_amount_correct() {
		Play hamlet = Play.createFromJson("hamlet", createPlayJson("Hamlet", "tragedy"));

		// Base: $400 × audience
		// Over 30: + $10 per seat over 30 ($0.10 × 100 cents)
		assertEquals(40000, hamlet.calculateAmount(30)); // $400 × 30 = $12,000 → 40000 cents
		assertEquals(45000, hamlet.calculateAmount(35)); // $400 × 35 + $10 × 5 = $14,500 → 45000 cents
	}

	@Test
	void comedy_amount_correct() {
		Play comedy = Play.createFromJson("as-like", createPlayJson("As You Like It", "comedy"));

		// audience=20: $300×20 + $3×20 = $360 = 36,000 cents
		assertEquals(36000, comedy.calculateAmount(20));

		// audience=25: $300×25 + $100 + $5×5 + $3×25 = $425 = 42,500 cents
		assertEquals(50000, comedy.calculateAmount(25));
	}

	@Test
	void tragedy_credits_correct() {
		Play hamlet = Play.createFromJson("hamlet", createPlayJson("Hamlet", "tragedy"));

		// 1 credit per seat over 30
		assertEquals(0, hamlet.calculateVolumeCredits(25));
		assertEquals(25, hamlet.calculateVolumeCredits(55));
	}

	@Test
	void comedy_credits_correct() {
		Play comedy = Play.createFromJson("as-like", createPlayJson("As You Like It", "comedy"));

		// 1 per seat over 30 + 1 per 5 seats
		assertEquals(5, comedy.calculateVolumeCredits(25)); // 0 over 30 + floor(25/5)
		assertEquals(48, comedy.calculateVolumeCredits(65)); // 35 over 30 + floor(65/5)
	}

	@Test
	void formatAmount_works() {
		Play play = Play.createFromJson("hamlet", createPlayJson("Hamlet", "tragedy"));

		assertEquals("$400.00", play.formatAmount(40000));
	}

	@Test
	void dollars_staticMethod_works() {
		assertEquals("$1,234.00", Play.dollars(123400));
	}

	// Test factory
	private JsonObject createPlayJson(String name, String type) {
		JsonObject play = new JsonObject();
		play.addProperty("name", name);
		play.addProperty("type", type);
		return play;
	}
}
