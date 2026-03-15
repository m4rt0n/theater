package com.marton.theater.models;

public class Tragedy extends Play {

	public Tragedy(String name) {
		super(name);
	}

	@Override
	public int amount(int audience) {
		// 30k base + 10k + 500/seat over 20 + 300/seat always.
		int result = 40000;
		if (audience > 30) {
			result += 1000 * (audience - 30);
		}
		return result;
	}

	@Override
	public int credits(int audience) {
		return Math.max(audience - 30, 0);
	}

}
