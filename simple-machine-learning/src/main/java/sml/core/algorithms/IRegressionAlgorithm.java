package sml.core.algorithms;

import org.ejml.simple.SimpleMatrix;

public interface IRegressionAlgorithm {
	double calculateCost(SimpleMatrix featureMatrix, SimpleMatrix yVector, SimpleMatrix thetaVector);

	double calculateHypothesis(SimpleMatrix featureRow, SimpleMatrix thetaVector);
}
