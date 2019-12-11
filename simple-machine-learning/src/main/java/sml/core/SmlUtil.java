package sml.core;

import org.ejml.simple.SimpleMatrix;

public class SmlUtil {

	public static SimpleMatrix createFeatureRow(double[] features) {
		int rows = 1;
		int cols = features.length + 1;
		SimpleMatrix featureRow = new SimpleMatrix(rows, cols);
		featureRow.set(0, 0, 1.0);
		featureRow.setRow(0, 1, features);
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
