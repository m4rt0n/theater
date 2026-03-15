package com.marton.theater;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marton.theater.models.StatementPrinter;
import com.marton.theater.utils.JsonLoader;

public class IntegrationTest {
	@TempDir
	Path tempDir;

	private Path invoicesFile;
	private Path playsFile;

	@BeforeEach
	void setUp() throws IOException {
		invoicesFile = tempDir.resolve("invoices.json");
		playsFile = tempDir.resolve("plays.json");
	}

	@Test
	void endToEnd_multipleInvoices(@TempDir Path tempDir) throws IOException {
		// Create test files
		writeInvoicesFile(invoicesFile, createMultipleInvoicesJson());
		writePlaysFile(playsFile, createPlaysJson());

		// Run main logic (without System.out)
		String result = runMainLogic(invoicesFile, playsFile);

		assertTrue(result.contains("Statement for BigCo"));
		assertTrue(result.contains("Statement for SecondCo"));
	}

	@Test
	void endToEnd_singleInvoice() throws IOException {
		writeInvoicesFile(invoicesFile, createSingleInvoiceJson());
		writePlaysFile(playsFile, createPlaysJson());

		String result = runMainLogic(invoicesFile, playsFile);

		assertTrue(result.contains("Statement for BigCo"));
	}

	private String runMainLogic(Path invoicesPath, Path playsPath) throws IOException {
		try (Reader invReader = Files.newBufferedReader(invoicesPath);
				Reader playsReader = Files.newBufferedReader(playsPath)) {

			JsonArray invoices = JsonLoader.loadInvoices(invReader);
			JsonObject plays = JsonLoader.loadPlays(playsReader);

			StatementPrinter printer = new StatementPrinter();
			return printer.print(invoices, plays);
		}
	}

	private void writeInvoicesFile(Path path, String content) throws IOException {
		Files.write(path, content.getBytes());
	}

	private void writePlaysFile(Path path, String content) throws IOException {
		Files.write(path, content.getBytes());
	}

	private String createMultipleInvoicesJson() {
		return """
				[
				  {"customer": "BigCo", "performances": [{"playID": "hamlet", "audience": 55}]},
				  {"customer": "SecondCo", "performances": [{"playID": "as-like", "audience": 35}]}
				]
				""";
	}

	private String createSingleInvoiceJson() {
		return """
				{"customer": "BigCo", "performances": [{"playID": "hamlet", "audience": 55}]}
				""";
	}

	private String createPlaysJson() {
		return """
				{
				  "hamlet": {"name": "Hamlet", "type": "tragedy"},
				  "as-like": {"name": "As You Like It", "type": "comedy"}
				}
				""";
	}
}
