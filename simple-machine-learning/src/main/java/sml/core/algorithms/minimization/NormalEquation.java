package sml.core.algorithms.minimization;

import org.ejml.simple.SimpleMatrix;

public class NormalEquation {

	// Returns Theta vector
	public static SimpleMatrix minimizeCostWithNormalEquation(SimpleMatrix featureMatrix, SimpleMatrix yVector) {
		SimpleMatrix xTransposed = featureMatrix.transpose();
		SimpleMatrix theta = (xTransposed.mult(featureMatrix)).invert().mult(xTransposed).mult(yVector);
		return theta;
	}

}
