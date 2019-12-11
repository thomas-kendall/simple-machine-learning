package sml.core;

import org.ejml.simple.SimpleMatrix;

public class LinearRegressionAlgorithm implements IRegressionAlgorithm {

	@Override
	public double calculateCost(SimpleMatrix featureMatrix, SimpleMatrix yVector, SimpleMatrix thetaVector) {
		double sum = 0.0;
		int m = featureMatrix.numRows();
		for (int i = 0; i < m; i++) {
			SimpleMatrix featureRow = featureMatrix.rows(i, i + 1);
			double h = calculateHypothesis(featureRow, thetaVector);
			double y = yVector.get(i, 0);
			sum += (h - y) * (h - y);
		}
		return sum / (m * 2);
	}

	@Override
	public double calculateHypothesis(SimpleMatrix featureRow, SimpleMatrix thetaVector) {
		return thetaVector.transpose().mult(featureRow.transpose()).get(0, 0);
	}

}
