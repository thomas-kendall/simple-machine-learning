package sml.core.algorithms.minimization;

import org.ejml.simple.SimpleMatrix;

import sml.core.IRegressionAlgorithm;

public class GradientDescent {

	// Returns Theta vector
	public static SimpleMatrix minimizeCostWithGradientDescent(IRegressionAlgorithm regressionAlgorithm, SimpleMatrix featureMatrix,
			SimpleMatrix yVector, SimpleMatrix thetaVector, double alpha, int numberOfIterations) {
		double[] costHistory = new double[numberOfIterations];
		for (int i = 0; i < numberOfIterations; i++) {
			SimpleMatrix newThetaVector = thetaVector.copy();
			for (int col = 0; col < featureMatrix.numCols(); col++) {
				double sum = 0.0;
				for (int row = 0; row < featureMatrix.numRows(); row++) {
					SimpleMatrix featureVector = featureMatrix.rows(row, row + 1).transpose();
					SimpleMatrix h = new SimpleMatrix(1, 1);
					h.set(0, 0, regressionAlgorithm.calculateHypothesis(featureVector.transpose(), thetaVector));
					sum += (h.minus(yVector.rows(row, row + 1)).mult(featureVector.rows(col, col + 1))).get(0);
				}
				double newTheta = thetaVector.get(col, 0) - alpha / featureMatrix.numRows() * sum;
				newThetaVector.set(col, 0, newTheta);
			}
			thetaVector = newThetaVector;
			costHistory[i] = regressionAlgorithm.calculateCost(featureMatrix, yVector, thetaVector);
		}

		return thetaVector;
	}

}
