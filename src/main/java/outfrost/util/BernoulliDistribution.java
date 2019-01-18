package outfrost.util;

import java.util.Random;

public class BernoulliDistribution {
	
	private double pSuccess;
	private Random r = new Random();
	
	public BernoulliDistribution(double pSuccess) {
		if (pSuccess < 0.0d || pSuccess > 1.0d) {
			throw new IllegalArgumentException("Success probability outside acceptable range");
		}
		this.pSuccess = pSuccess;
	}
	
	public boolean nextBoolean() {
		return r.nextDouble() < pSuccess;
	}
	
}
