package com.marton.theater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.marton.theater.exceptions.InvalidPlayTypeException;
import com.marton.theater.models.Comedy;
import com.marton.theater.models.Play;
import com.marton.theater.models.Tragedy;

public class PlayTest {
	@Test
	void tragedyAmount55Seats() {
		Play hamlet = new Tragedy("Hamlet");
		assertEquals(65000, hamlet.amount(55), "Hamlet 55 seats: $650");
	}

	@Test
	void tragedyAmount30Seats() {
		Play hamlet = new Tragedy("Hamlet");
		assertEquals(40000, hamlet.amount(30), "Base rate only");
	}

	@Test
	void tragedyAmount20Seats() {
		Play hamlet = new Tragedy("Hamlet");
		assertEquals(40000, hamlet.amount(20), "Under 30 seats");
	}

	@Test
	void comedyAmount35Seats() {
		Play comedy = new Comedy("As You Like It");
		assertEquals(58000, comedy.amount(35), "As You Like It 35 seats: $580");
	}

	@Test
	void comedyAmount15Seats() {
		Play comedy = new Comedy("Comedy");
		assertEquals(34500, comedy.amount(15), "Per-seat bonus only");
	}

	@Test
	void tragedyCredits55Seats() {
		Play tragedy = new Tragedy("Tragedy");
		assertEquals(25, tragedy.credits(55), "55-30 volume credits");
	}

	@Test
	void tragedyCredits25Seats() {
		Play tragedy = new Tragedy("Tragedy");
		assertEquals(0, tragedy.credits(25), "No volume credits");
	}

	@Test
	void comedyCredits35Seats() {
		Play comedy = new Comedy("Comedy");
		assertEquals(12, comedy.credits(35), "5 volume + 7 comedy bonus");
	}

	@Test
	void comedyCredits25Seats() {
		Play comedy = new Comedy("Comedy");
		assertEquals(0, comedy.credits(25), "No volume credits");
	}

	@Test
	void playFactoryCreatesTragedy() {
		JsonObject data = new JsonObject();
		data.addProperty("name", "Hamlet");
		data.addProperty("type", "tragedy");
		Play play = Play.createFromJson("hamlet", data);
		assertInstanceOf(Tragedy.class, play);
	}

	@Test
	void playFactoryCreatesComedy() {
		JsonObject data = new JsonObject();
		data.addProperty("name", "Comedy");
		data.addProperty("type", "comedy");
		Play play = Play.createFromJson("comedy", data);
		assertInstanceOf(Comedy.class, play);
	}

	@Test
	void playFactoryRejectsUnknownType() {
		JsonObject data = new JsonObject();
		data.addProperty("name", "History");
		data.addProperty("type", "history");
		assertThrows(InvalidPlayTypeException.class, () -> Play.createFromJson("history", data));
	}

	@Test
	void moneyFormatting() {
		assertEquals("$650.00", Play.dollars(65000));
		assertEquals("$580.00", Play.dollars(58000));
		assertEquals("$1730.00", Play.dollars(173000));
		assertEquals("$400.00", Play.dollars(40000));
	}
}
