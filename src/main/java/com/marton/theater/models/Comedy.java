package com.marton.theater.models;

public class Comedy extends Play {

	public Comedy(String name) {
		super(name);
	}

	@Override
	public int amount(int audience) {
		// $30k base + $10k + $500/seat over 20 + $300/seat always.
		int result = 30000;
		if (audience > 20) {
			result += 10000 + 500 * (audience - 20);
		}
		result += 300 * audience; // $0.50/seat bonus
		return result;
	}

	@Override
	public int credits(int audience) {
		// Volume credits + comedy bonus (1 per 5 seats)
		return Math.max(audience - 30, 0) + (int) Math.floor(audience / 5.0);
	}

}
