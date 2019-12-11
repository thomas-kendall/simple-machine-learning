package sml.core;

import org.ejml.simple.SimpleMatrix;

public class NormalizationParameters {

	private SimpleMatrix mu; // The mean of each column
	private SimpleMatrix sigma; // The standard deviation of each column

	public NormalizationParameters(SimpleMatrix mu, SimpleMatrix sigma) {
		super();
		this.mu = mu;
		this.sigma = sigma;
	}

	public SimpleMatrix getMu() {
		return mu;
	}

	public SimpleMatrix getSigma() {
		return sigma;
	}
}
