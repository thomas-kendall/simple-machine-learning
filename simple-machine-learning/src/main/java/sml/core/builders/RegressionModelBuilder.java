package sml.core.builders;

import org.ejml.simple.SimpleMatrix;

import sml.core.IRegressionModel;
import sml.core.RegressionModel;
import sml.core.algorithms.IRegressionAlgorithm;
import sml.core.algorithms.LinearRegressionAlgorithm;
import sml.core.algorithms.LogisticRegressionAlgorithm;
import sml.core.algorithms.training.GradientDescentTrainingAlgorithm;
import sml.core.algorithms.training.ITrainingAlgorithm;
import sml.core.algorithms.training.NormalEquationTrainingAlgorithm;
import sml.core.normalization.NormalizationParameters;

public class RegressionModelBuilder {

	private IRegressionAlgorithm regressionAlgorithm;

	private boolean isGradientDescent;

	// Gradient Descent options
	private double alpha;
	private int numberOfIterations;
	private SimpleMatrix initialThetaVector;

	// Pre-trained options
	private SimpleMatrix thetaVector;
	private NormalizationParameters normalizationParameters;

	private boolean applyNormalization;

	public RegressionModelBuilder() {
		regressionAlgorithm = null;
		isGradientDescent = false;
		alpha = 0.0;
		numberOfIterations = 0;
		initialThetaVector = null;
	}

	public IRegressionModel build() {
		if (regressionAlgorithm == null) {
			throw new RuntimeException("A Regression Algorithm is required.");
		}

		IRegressionModel result;
		if (thetaVector == null) {
			// It is not pre-trained
			ITrainingAlgorithm trainingAlgorithm = isGradientDescent
					? new GradientDescentTrainingAlgorithm(regressionAlgorithm, alpha, numberOfIterations,
							initialThetaVector)
					: new NormalEquationTrainingAlgorithm();
			result = new RegressionModel(regressionAlgorithm, trainingAlgorithm, applyNormalization);
		} else {
			// It is pre-trained
			result = new RegressionModel(regressionAlgorithm, thetaVector, normalizationParameters);
		}

		return result;
	}

	public RegressionModelBuilder withGradientDescentTraining(double alpha, int numberOfIterations) {
		isGradientDescent = true;
		this.alpha = alpha;
		this.numberOfIterations = numberOfIterations;
		this.initialThetaVector = null;
		return this;
	}

	public RegressionModelBuilder withGradientDescentTraining(double alpha, int numberOfIterations,
			SimpleMatrix initialThetaVector) {
		isGradientDescent = true;
		this.alpha = alpha;
		this.numberOfIterations = numberOfIterations;
		this.initialThetaVector = initialThetaVector;
		return this;
	}

	public RegressionModelBuilder withLinearRegression() {
		this.regressionAlgorithm = new LinearRegressionAlgorithm();
		return this;
	}

	public RegressionModelBuilder withLogisticRegression() {
		this.regressionAlgorithm = new LogisticRegressionAlgorithm();
		return this;
	}

	public RegressionModelBuilder withNormalEquationTraining() {
		isGradientDescent = false;
		return this;
	}

	public RegressionModelBuilder withNormalization() {
		applyNormalization = true;
		return this;
	}

	public RegressionModelBuilder withoutNormalization() {
		applyNormalization = false;
		return this;
	}

	public RegressionModelBuilder withPreTrained(SimpleMatrix thetaVector,
			NormalizationParameters normalizationParameters) {
		this.thetaVector = thetaVector;
		this.normalizationParameters = normalizationParameters;
		return this;
	}
}
