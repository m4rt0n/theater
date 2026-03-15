package com.marton.theater;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marton.theater.exceptions.TheaterException;
import com.marton.theater.models.StatementPrinter;
import com.marton.theater.utils.JsonLoader;

public class TheaterMainApp {

	public static void main(String[] args) throws Exception {
		try {

			Reader invoicesReader = new FileReader("invoices.json");
			Reader playsReader = new FileReader("plays.json");

			JsonArray rawInvoices = JsonLoader.loadInvoices(invoicesReader);
			JsonObject rawPlays = JsonLoader.loadPlays(playsReader);

			StatementPrinter printer = new StatementPrinter();
			String statements = printer.print(rawInvoices, rawPlays);
			System.out.println(statements);

		} catch (IOException e) {
			System.err.println("File error (" + e.getClass().getSimpleName() + "): " + e.getMessage());
			System.exit(1);
		} catch (TheaterException e) {
			System.err.println("Theater billing error: " + e.getMessage());
			System.exit(1);
		}
	}

}
