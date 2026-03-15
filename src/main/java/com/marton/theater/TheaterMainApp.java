package com.marton.theater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marton.theater.exceptions.TheaterException;
import com.marton.theater.models.StatementPrinter;

public class TheaterMainApp {

	public static void main(String[] args) throws Exception {
		try {
			JsonArray rawInvoices = parseInvoicesFile();
			JsonObject rawPlays = parsePlaysFile();

			StatementPrinter printer = new StatementPrinter();
			String result = printer.print(rawInvoices, rawPlays);
			System.out.println(result);

		} catch (IOException e) {
			System.err.println("File error (" + e.getClass().getSimpleName() + "): " + e.getMessage());
			System.exit(1);
		} catch (TheaterException e) {
			System.err.println("Theater billing error: " + e.getMessage());
			System.exit(1);
		}
	}

	private static JsonArray parseInvoicesFile() throws Exception {
		String invoicesJson = new String(Files.readAllBytes(new File("invoices.json").toPath()));
		return JsonParser.parseString(invoicesJson).getAsJsonArray();
	}

	private static JsonObject parsePlaysFile() throws Exception {
		String playsJson = new String(Files.readAllBytes(new File("plays.json").toPath()));
		return JsonParser.parseString(playsJson).getAsJsonObject();
	}

}
