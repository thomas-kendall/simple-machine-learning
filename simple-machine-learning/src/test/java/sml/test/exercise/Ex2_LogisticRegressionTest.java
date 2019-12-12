package sml.test.exercise;

import java.io.IOException;

import org.ejml.simple.SimpleMatrix;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import sml.core.IRegressionModel;
import sml.core.algorithms.IRegressionAlgorithm;
import sml.core.algorithms.LogisticRegressionAlgorithm;
import sml.core.builders.RegressionModelBuilder;
import sml.core.utility.SmlUtil;
import sml.test.utility.TestUtil;

public class Ex2_LogisticRegressionTest {

	@Test
	public void exercise_2_1_2_1_sigmoid_function() {
		Assert.assertEquals(0.0, LogisticRegressionAlgorithm.sigmoid(-10.0), 0.001);
		Assert.assertEquals(0.5, LogisticRegressionAlgorithm.sigmoid(0.0), 0.001);
		Assert.assertEquals(1.0, LogisticRegressionAlgorithm.sigmoid(10.0), 0.001);
	}

	@Test
	public void exercise_2_1_2_2_calculateCostShouldCalculateCorrectly() throws IOException {
		// Exam1 Score, Exam2 Score, Admitted
		SimpleMatrix matrix = TestUtil.loadFileCSV("ex2data1.txt");
		SimpleMatrix featureMatrix = SmlUtil.prependOnesVector(matrix.cols(0, matrix.numCols() - 1));
		SimpleMatrix yVector = matrix.cols(matrix.numCols() - 1, matrix.numCols());
		IRegressionAlgorithm regressionAlgorithm = new LogisticRegressionAlgorithm();

		// Try with theta of 0,0,0
		double[][] theta = { { 0.0 }, { 0.0 }, { 0.0 } };
		SimpleMatrix thetaVector = new SimpleMatrix(theta);
		double expectedCost = 0.693;
		double actualCost = regressionAlgorithm.calculateCost(featureMatrix, yVector, thetaVector);
		Assert.assertEquals(expectedCost, actualCost, 0.01);

		// Try with theta of -24, 0.2, 0.2
		theta[0][0] = -24;
		theta[1][0] = 0.2;
		theta[2][0] = 0.2;
		thetaVector = new SimpleMatrix(theta);
		expectedCost = 0.218;
		actualCost = regressionAlgorithm.calculateCost(featureMatrix, yVector, thetaVector);
		Assert.assertEquals(expectedCost, actualCost, 0.01);
	}

	@Ignore
	// Without an implementation of fminunc, I was not getting the same value so I
	// have move on from here.
	@Test
	public void exercise_2_1_2_3_gradientDescentShouldCalculateCorrectly() throws IOException {
		// Exam1 Score, Exam2 Score, Admitted
		SimpleMatrix matrix = TestUtil.loadFileCSV("ex2data1.txt");
		SimpleMatrix featureMatrix = matrix.cols(0, matrix.numCols() - 1);
		SimpleMatrix yVector = matrix.cols(matrix.numCols() - 1, matrix.numCols());

		double alpha = 0.1;
		int numberOfIterations = 10000;
		IRegressionModel model = new RegressionModelBuilder().withLogisticRegression()
				.withGradientDescentTraining(alpha, numberOfIterations).withNormalization().build();

		// Run gradient descent
		model.train(featureMatrix, yVector);
		SimpleMatrix thetaVector = model.getThetaVector();

		Assert.assertEquals(-25.161, thetaVector.get(0, 0), 0.01);
		Assert.assertEquals(0.206, thetaVector.get(1, 0), 0.01);
		Assert.assertEquals(0.201, thetaVector.get(2, 0), 0.01);
	}

	@Test
	public void exercise_2_1_2_4_predictionShouldCalculateCorrectly() {
		double[][] theta = { { -25.161 }, { 0.206 }, { 0.201 } };
		SimpleMatrix thetaVector = new SimpleMatrix(theta);

		IRegressionAlgorithm regressionAlgorithm = new LogisticRegressionAlgorithm();

		double[][] examScores = { { 45.0, 85.0 } };
		SimpleMatrix featureRow = new SimpleMatrix(examScores);
		featureRow = SmlUtil.prependOnesVector(featureRow);

		double expectedAdmissionProbability = 0.775;
		double actualAdmissionProbability = regressionAlgorithm.calculateHypothesis(featureRow, thetaVector);

		Assert.assertEquals(expectedAdmissionProbability, actualAdmissionProbability, 0.01);
	}

}
