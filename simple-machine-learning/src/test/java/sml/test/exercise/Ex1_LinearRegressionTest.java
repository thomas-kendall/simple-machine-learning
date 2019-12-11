package sml.test.exercise;

import java.io.IOException;

import org.ejml.simple.SimpleMatrix;
import org.junit.Assert;
import org.junit.Test;

import sml.core.LinearRegressionAlgorithm;
import sml.core.Normalization;
import sml.core.NormalizationParameters;
import sml.core.SmlUtil;
import sml.core.algorithms.minimization.GradientDescent;
import sml.core.algorithms.minimization.NormalEquation;
import sml.test.utility.TestUtil;

public class Ex1_LinearRegressionTest {

	@Test
	public void calculateHypothesisShouldCalculateCorrectly1() {
		double[] features = new double[] { 1.0, 2.0, 3.0 };
		double[] theta = new double[] { 1.0, 3.0, 5.0, 10.0 };
		SimpleMatrix featureRow = SmlUtil.createFeatureRow(features);
		SimpleMatrix thetaVector = SmlUtil.createVector(theta.length);
		thetaVector.setColumn(0, 0, theta);
		double expectedHypothesis = 44.0;
		double actualHypothesis = new LinearRegressionAlgorithm().calculateHypothesis(featureRow, thetaVector);
		Assert.assertEquals(expectedHypothesis, actualHypothesis, 0.0001);
	}

	@Test
	public void calculateHypothesisShouldCalculateCorrectly2() {
		double[] features = { -0.44127, -0.22368 };
		double[] theta = new double[] { 338658.249249, 104127.515597, -172.205334 };
		SimpleMatrix featureRow = SmlUtil.createFeatureRow(features);
		SimpleMatrix thetaVector = SmlUtil.createVector(theta.length);
		thetaVector.setColumn(0, 0, theta);

		double expectedHypothesis = 292748.08;
		double actualHypothesis = new LinearRegressionAlgorithm().calculateHypothesis(featureRow, thetaVector);
		Assert.assertEquals(expectedHypothesis, actualHypothesis, 1.0);
	}

	@Test
	public void exercise_2_2_3_1_calculateCostShouldCalculateCorrectly() throws IOException {
		// Size of house in square feet, House Price
		SimpleMatrix matrix = TestUtil.loadFileCSV("ex1data1.txt");
		SimpleMatrix featureMatrix = SmlUtil.prependOnesVector(matrix.cols(0, matrix.numCols() - 1));
		SimpleMatrix yVector = matrix.cols(matrix.numCols() - 1, matrix.numCols());

		// Try with theta of 0,0
		double[][] theta = { { 0.0 }, { 0.0 } };
		SimpleMatrix thetaVector = new SimpleMatrix(theta);
		double expectedCost = 32.07;
		double actualCost = new LinearRegressionAlgorithm().calculateCost(featureMatrix, yVector, thetaVector);
		Assert.assertEquals(expectedCost, actualCost, 0.01);
	}

	@Test
	public void exercise_2_2_3_2_calculateCostShouldCalculateCorrectly() throws IOException {
		// Size of house in square feet, House Price
		SimpleMatrix matrix = TestUtil.loadFileCSV("ex1data1.txt");
		SimpleMatrix featureMatrix = SmlUtil.prependOnesVector(matrix.cols(0, matrix.numCols() - 1));
		SimpleMatrix yVector = matrix.cols(matrix.numCols() - 1, matrix.numCols());

		// Try with theta of -1,2
		double[][] theta = { { -1.0 }, { 2.0 } };
		SimpleMatrix thetaVector = new SimpleMatrix(theta);
		double expectedCost = 54.24;
		double actualCost = new LinearRegressionAlgorithm().calculateCost(featureMatrix, yVector, thetaVector);
		Assert.assertEquals(expectedCost, actualCost, 0.01);
	}

	@Test
	public void exercise_3_1_calculateNormalizationParametersShouldCalculateCorrectly() throws IOException {
		// Size of house in square feet, Number of bedrooms, House Price
		SimpleMatrix matrix = TestUtil.loadFileCSV("ex1data2.txt");
		SimpleMatrix featureMatrix = matrix.cols(0, matrix.numCols() - 1);

		double[][] expectedMu = { { 2000.6809, 3.1702 } };
		double[][] expectedSigma = { { 794.70235, 0.76098 } };

		// Normalize features
		NormalizationParameters normalizationParameters = Normalization.calculateNormalizationParameters(featureMatrix);

		Assert.assertEquals(expectedMu[0][0], normalizationParameters.getMu().get(0, 0), 0.001);
		Assert.assertEquals(expectedMu[0][1], normalizationParameters.getMu().get(0, 1), 0.001);
		Assert.assertEquals(expectedSigma[0][0], normalizationParameters.getSigma().get(0, 0), 0.001);
		Assert.assertEquals(expectedSigma[0][1], normalizationParameters.getSigma().get(0, 1), 0.001);
	}

	@Test
	public void exercise_3_1_normalizeFeaturesShouldCalculateCorrectly() {
		double[][] features = { { 1650.0, 3.0 } };
		SimpleMatrix featureRow = new SimpleMatrix(features);

		double[][] mu = { { 2000.6809, 3.1702 } };
		double[][] sigma = { { 794.70235, 0.76098 } };
		NormalizationParameters normalizationParameters = new NormalizationParameters(new SimpleMatrix(mu),
				new SimpleMatrix(sigma));

		double[][] expectedFeaturesNormalized = { { -0.44127, -0.22368 } };
		featureRow = Normalization.normalizeFeatures(featureRow, normalizationParameters);
		Assert.assertEquals(expectedFeaturesNormalized[0][0], featureRow.get(0, 0), 0.001);
		Assert.assertEquals(expectedFeaturesNormalized[0][1], featureRow.get(0, 1), 0.001);
	}

	@Test
	public void exercise_3_2_gradientDescentShouldCalculateCorrectly() throws IOException {
		// Size of house in square feet, Number of bedrooms, House Price
		SimpleMatrix matrix = TestUtil.loadFileCSV("ex1data2.txt");
		SimpleMatrix featureMatrix = matrix.cols(0, matrix.numCols() - 1);
		SimpleMatrix yVector = matrix.cols(matrix.numCols() - 1, matrix.numCols());

		// Normalize features
		NormalizationParameters normalizationParameters = Normalization.calculateNormalizationParameters(featureMatrix);
		featureMatrix = Normalization.normalizeFeatures(featureMatrix, normalizationParameters);

		// Add the intercept term
		featureMatrix = SmlUtil.prependOnesVector(featureMatrix);

		// Run gradient descent
		double alpha = 0.1;
		int numberOfIterations = 50;
		SimpleMatrix thetaVector = new SimpleMatrix(3, 1);
		thetaVector = GradientDescent.minimizeCostWithGradientDescent(new LinearRegressionAlgorithm(), featureMatrix,
				yVector, thetaVector, alpha, numberOfIterations);

		// Predict cost of house
		double[][] features = { { 1650.0, 3.0 } };
		SimpleMatrix featureRow = new SimpleMatrix(features);
		featureRow = Normalization.normalizeFeatures(featureRow, normalizationParameters);
		featureRow = SmlUtil.prependOnesVector(featureRow);
		double expectedPrice = 292748.085;
		double actualPrice = new LinearRegressionAlgorithm().calculateHypothesis(featureRow, thetaVector);
		Assert.assertEquals(expectedPrice, actualPrice, 0.001);
	}

	@Test
	public void exercise_3_3_normalEquationShouldCalculateCorrectly() throws IOException {
		// Size of house in square feet, Number of bedrooms, House Price
		SimpleMatrix matrix = TestUtil.loadFileCSV("ex1data2.txt");
		SimpleMatrix featureMatrix = matrix.cols(0, matrix.numCols() - 1);
		SimpleMatrix yVector = matrix.cols(matrix.numCols() - 1, matrix.numCols());
		featureMatrix = SmlUtil.prependOnesVector(featureMatrix);

		// Minimize the cost function
		SimpleMatrix thetaVector = NormalEquation.minimizeCostWithNormalEquation(featureMatrix, yVector);

		// Predict 1650 square feet, 3 bedrooms
		double[][] features = { { 1650.0, 3.0 } };
		SimpleMatrix featureRow = new SimpleMatrix(features);
		featureRow = SmlUtil.prependOnesVector(featureRow);
		double expectedHypothesis = 293081.464;
		double actualHypothesis = new LinearRegressionAlgorithm().calculateHypothesis(featureRow, thetaVector);
		Assert.assertEquals(expectedHypothesis, actualHypothesis, 0.1);
	}
}