package sml.core;

import org.ejml.simple.SimpleMatrix;

import sml.core.algorithms.IRegressionAlgorithm;
import sml.core.algorithms.training.ITrainingAlgorithm;
import sml.core.normalization.Normalization;
import sml.core.normalization.NormalizationParameters;
import sml.core.utility.SmlUtil;

public class RegressionModel implements IRegressionModel {

	private IRegressionAlgorithm regressionAlgorithm;
	private ITrainingAlgorithm trainingAlgorithm;
	private SimpleMatrix thetaVector;
	private boolean applyNormalization;
	private NormalizationParameters normalizationParameters;

	// Un-trained
	public RegressionModel(IRegressionAlgorithm regressionAlgorithm, ITrainingAlgorithm trainingAlgorithm,
			boolean applyNormalization) {
		this.regressionAlgorithm = regressionAlgorithm;
		this.trainingAlgorithm = trainingAlgorithm;
		this.thetaVector = null;
		this.normalizationParameters = null;
		this.applyNormalization = applyNormalization;
	}

	// Pre-trained
	public RegressionModel(IRegressionAlgorithm regressionAlgorithm, SimpleMatrix thetaVector,
			NormalizationParameters normalizationParameters) {
		this.regressionAlgorithm = regressionAlgorithm;
		this.trainingAlgorithm = null;
		this.thetaVector = thetaVector;
		this.normalizationParameters = normalizationParameters;
		this.applyNormalization = this.normalizationParameters != null;
	}

	@Override
	public double calculateHypothesis(SimpleMatrix featureRow) {
		if (!isTrained()) {
			throw new RuntimeException("The model has not been trained.");
		}

		if (normalizationParameters != null) {
			featureRow = Normalization.normalizeFeatures(featureRow, normalizationParameters);
		}
		featureRow = SmlUtil.prependOnesVector(featureRow);
		return regressionAlgorithm.calculateHypothesis(featureRow, thetaVector);
	}

	@Override
	public NormalizationParameters getNormalizationParameters() {
		return normalizationParameters;
	}

	@Override
	public SimpleMatrix getThetaVector() {
		return thetaVector;
	}

	private boolean isTrained() {
		return thetaVector != null;
	}

	@Override
	public void train(SimpleMatrix featureMatrix, SimpleMatrix yVector) {
		if (trainingAlgorithm == null) {
			throw new RuntimeException("The model has not been setup for training.");
		}

		if (applyNormalization) {
			normalizationParameters = Normalization.calculateNormalizationParameters(featureMatrix);
			featureMatrix = Normalization.normalizeFeatures(featureMatrix, normalizationParameters);
		} else {
			normalizationParameters = null;
		}
		featureMatrix = SmlUtil.prependOnesVector(featureMatrix);
		thetaVector = trainingAlgorithm.train(featureMatrix, yVector);
	}
}
