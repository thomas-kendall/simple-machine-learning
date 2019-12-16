package sml.core.algorithms.training;

import org.ejml.simple.SimpleMatrix;

import sml.core.algorithms.IRegressionAlgorithm;

public class GradientDescentTrainingAlgorithm implements ITrainingAlgorithm {
	private IRegressionAlgorithm regressionAlgorithm;
	private double alpha;
	private int numberOfIterations;
	private SimpleMatrix initialThetaVector;

	public GradientDescentTrainingAlgorithm(IRegressionAlgorithm regressionAlgorithm, double alpha,
			int numberOfIterations, SimpleMatrix initialThetaVector) {
		this.regressionAlgorithm = regressionAlgorithm;
		this.alpha = alpha;
		this.numberOfIterations = numberOfIterations;
		this.initialThetaVector = null;
		this.initialThetaVector = initialThetaVector;
	}

	private SimpleMatrix getInitialThetaVector(int numRows) {
		if (initialThetaVector == null) {
			initialThetaVector = new SimpleMatrix(numRows, 1);
		}

		return initialThetaVector;
	}

	@Override
	public SimpleMatrix train(SimpleMatrix featureMatrix, SimpleMatrix yVector) {
		SimpleMatrix thetaVector = getInitialThetaVector(featureMatrix.numCols());
		double[] costHistory = new double[numberOfIterations];
		for (int i = 0; i < numberOfIterations; i++) {
			System.out.println("Training iteration " + i);
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
