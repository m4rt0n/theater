package com.marton.theater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonObject;
import com.marton.theater.domain.Performance;
import com.marton.theater.exceptions.InvalidPerformanceException;

@ExtendWith(MockitoExtension.class)
class PerformanceTest {

	@Mock
	private Map<String, JsonObject> mockPlays; // External JSON dependency

	private JsonObject hamletJson;
	private JsonObject comedyJson;

	@BeforeEach
	void setUp() {
		hamletJson = createPlayJson("Hamlet", "tragedy");
		comedyJson = createPlayJson("As You Like It", "comedy");
	}

	@Test
    void delegatesAmountCalculationToPlay() {
        
        when(mockPlays.get("hamlet")).thenReturn(hamletJson);
        when(mockPlays.containsKey("hamlet")).thenReturn(true);
        
        
        Performance perf = new Performance("hamlet", 55, mockPlays);
        
        
        assertEquals(65000, perf.calculateAmount(), "Should delegate to TragedyPlay.amount(55)");
        verify(mockPlays).get("hamlet");  
    }

	@Test
    void delegatesCreditsCalculationToPlay() {
        
        when(mockPlays.get("hamlet")).thenReturn(hamletJson);
        when(mockPlays.containsKey("hamlet")).thenReturn(true);
        
        Performance perf = new Performance("hamlet", 55, mockPlays);
                
        assertEquals(25, perf.calculateVolumeCredits(), "Should delegate to TragedyPlay.credits(55)");
    }

	@Test
    void formatsPerformanceLineCorrectly() {
        when(mockPlays.get("hamlet")).thenReturn(hamletJson);
        when(mockPlays.containsKey("hamlet")).thenReturn(true);
        
        Performance perf = new Performance("hamlet", 55, mockPlays);
        
        assertEquals(" Hamlet: $650.00 (55 seats)", perf.formatLine(),
            "Should format play name, amount, and audience");
    }

	@Test
	void rejectsNullPlayID() {
		InvalidPerformanceException exception = assertThrows(InvalidPerformanceException.class,
				() -> new Performance(null, 55, mockPlays));
		assertEquals("Invalid performance: playID is required", exception.getMessage());
	}

	@Test
	void rejectsEmptyPlayID() {
		InvalidPerformanceException exception = assertThrows(InvalidPerformanceException.class,
				() -> new Performance("", 55, mockPlays));
		assertTrue(exception.getMessage().contains("playID is required"));
	}

	@Test
	void rejectsNegativeAudience() {
		InvalidPerformanceException exception = assertThrows(InvalidPerformanceException.class,
				() -> new Performance("hamlet", -1, mockPlays));
		assertTrue(exception.getMessage().contains("audience cannot be negative"));
	}

	@Test
    void rejectsUnknownPlayID() {
        when(mockPlays.containsKey("unknown")).thenReturn(false);
        
        InvalidPerformanceException exception = assertThrows(
            InvalidPerformanceException.class,
            () -> new Performance("unknown", 55, mockPlays));
        assertEquals("Invalid performance: Unknown playID: unknown", exception.getMessage());
    }

	@Test
    void comedyPerformanceCalculatesCorrectly() {
        when(mockPlays.get("as-like")).thenReturn(comedyJson);
        when(mockPlays.containsKey("as-like")).thenReturn(true);
        
        Performance perf = new Performance("as-like", 35, mockPlays);
        
        assertEquals(58000, perf.calculateAmount(), "Comedy 35 seats: $580");
        assertEquals(12, perf.calculateVolumeCredits(), "Comedy credits: 5 + 7 bonus");
        assertEquals(" As You Like It: $580.00 (35 seats)", perf.formatLine());
    }

	private JsonObject createPlayJson(String name, String type) {
		JsonObject json = new JsonObject();
		json.addProperty("name", name);
		json.addProperty("type", type);
		return json;
	}
}