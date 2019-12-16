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

	public static double[] getVectorData(SimpleMatrix vector) {
		double[] data = new double[vector.getNumElements()];
		for (int i = 0; i < vector.getNumElements(); i++) {
			data[i] = vector.get(i);
		}
		return data;
	}

	// TODO: Move to a shared class
	public static SimpleMatrix prependOnesVector(SimpleMatrix featureMatrix) {
		SimpleMatrix onesVector = new SimpleMatrix(featureMatrix.numRows(), 1);
		onesVector.set(1.0);
		return onesVector.concatColumns(featureMatrix);
	}

	public static double sigmoid(double z) {
		return 1.0 / (1 + Math.exp(-z));
	}

	public static SimpleMatrix sigmoid(SimpleMatrix matrix) {
		SimpleMatrix result = matrix.copy();
		for (int i = 0; i < result.getNumElements(); i++) {
			result.set(i, sigmoid(result.get(i)));
		}
		return result;
	}

}
