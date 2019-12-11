package sml.core;

import org.ejml.simple.SimpleMatrix;

public class Normalization {

	public static NormalizationParameters calculateNormalizationParameters(SimpleMatrix featureMatrix) {
		SimpleMatrix mu = new SimpleMatrix(1, featureMatrix.numCols());
		SimpleMatrix sigma = new SimpleMatrix(1, featureMatrix.numCols());

		for (int col = 0; col < featureMatrix.numCols(); col++) {
			// Calculate mu (mean)
			double sum = 0.0;
			for (int row = 0; row < featureMatrix.numRows(); row++) {
				sum += featureMatrix.get(row, col);
			}
			double mean = sum / featureMatrix.numRows();
			mu.set(0, col, mean);

			// Calculate sigma (standard deviation)
			sum = 0.0;
			for (int row = 0; row < featureMatrix.numRows(); row++) {
				sum += Math.pow(featureMatrix.get(row, col) - mu.get(0, col), 2);
			}
			double standardDeviation = Math.sqrt(sum / (featureMatrix.numRows() - 1));
			sigma.set(0, col, standardDeviation);
		}

		return new NormalizationParameters(mu, sigma);
	}

	public static SimpleMatrix normalizeFeatures(SimpleMatrix featureMatrix,
			NormalizationParameters normalizationParameters) {
		SimpleMatrix normalizedFeatureMatrix = featureMatrix.copy();

		for (int col = 0; col < normalizedFeatureMatrix.numCols(); col++) {
			for (int row = 0; row < normalizedFeatureMatrix.numRows(); row++) {
				double val = normalizedFeatureMatrix.get(row, col);
				double normalizedVal = (val - normalizationParameters.getMu().get(0, col))
						/ normalizationParameters.getSigma().get(0, col);
				normalizedFeatureMatrix.set(row, col, normalizedVal);
			}
		}

		return normalizedFeatureMatrix;
	}

}
