package sml.core;

import org.ejml.simple.SimpleMatrix;

import sml.core.normalization.NormalizationParameters;

public interface IRegressionModel {

	double calculateHypothesis(SimpleMatrix featureRow);

	NormalizationParameters getNormalizationParameters();

	SimpleMatrix getThetaVector();

	void train(SimpleMatrix featureMatrix, SimpleMatrix yVector);
}
