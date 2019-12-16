package sml.core.algorithms;

import org.ejml.simple.SimpleMatrix;

import sml.core.utility.SmlUtil;

public class LogisticRegressionAlgorithm implements IRegressionAlgorithm {

	@Override
	public double calculateCost(SimpleMatrix featureMatrix, SimpleMatrix yVector, SimpleMatrix thetaVector) {
		double sum = 0.0;
		int m = featureMatrix.numRows();
		for (int i = 0; i < m; i++) {
			SimpleMatrix featureRow = featureMatrix.rows(i, i + 1);
			double h = calculateHypothesis(featureRow, thetaVector);
			double y = yVector.get(i, 0);
			sum += -y * Math.log(h) - (1 - y) * Math.log(1 - h);
		}
		return sum / m;
	}

	@Override
	public double calculateHypothesis(SimpleMatrix featureRow, SimpleMatrix thetaVector) {
		return SmlUtil.sigmoid(thetaVector.transpose().mult(featureRow.transpose()).get(0, 0));
	}

}
