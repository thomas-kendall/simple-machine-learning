package sml.core.algorithms.training;

import org.ejml.simple.SimpleMatrix;

public class NormalEquationTrainingAlgorithm implements ITrainingAlgorithm {

	@Override
	public SimpleMatrix train(SimpleMatrix featureMatrix, SimpleMatrix yVector) {
		SimpleMatrix xTransposed = featureMatrix.transpose();
		SimpleMatrix theta = (xTransposed.mult(featureMatrix)).invert().mult(xTransposed).mult(yVector);
		return theta;
	}

}
