package com.marton.theater.domain;

public class Tragedy extends Play {

	public Tragedy(String name) {
		super(name);
	}

	@Override
	public int calculateAmount(int audience) {
		// 30k base + 10k + 500/seat over 20 + 300/seat always.
		int result = 40000;
		if (audience > 30) {
			result += 1000 * (audience - 30);
		}
		return result;
	}

	@Override
	public int calculateVolumeCredits(int audience) {
		return Math.max(audience - 30, 0);
	}

}
