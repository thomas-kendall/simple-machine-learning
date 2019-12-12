package sml.core.utility;

import org.ejml.simple.SimpleMatrix;

public class SmlUtil {

	public static SimpleMatrix createFeatureMatrix(double[][] features) {
		SimpleMatrix featureMatrix = new SimpleMatrix(features);
		featureMatrix = prependOnesVector(featureMatrix);
		return featureMatrix;
	}

	public static SimpleMatrix createRow(double[] features) {
		int rows = 1;
		int cols = features.length;
		SimpleMatrix featureRow = new SimpleMatrix(rows, cols);
		featureRow.setRow(0, 0, features);
		return featureRow;
	}

	public static SimpleMatrix createVector(int numberOfRows) {
		SimpleMatrix thetaVector = new SimpleMatrix(numberOfRows, 1);
		return thetaVector;
	}

	// TODO: Move to a shared class
	public static SimpleMatrix prependOnesVector(SimpleMatrix featureMatrix) {
		SimpleMatrix onesVector = new SimpleMatrix(featureMatrix.numRows(), 1);
		onesVector.set(1.0);
		return onesVector.concatColumns(featureMatrix);
	}

}
