# simple-machine-learning
This project was developed to help with machine learning at a very immature level. With only a few concepts, such as linear regression, logistic regression, gradient descent, normal equation, normalization, and others, this project can perform training and hypothesis calculations.

# Getting started
To interact with the library, use the RegressionModelBuilder class like so:
```
		IRegressionModel model = new RegressionModelBuilder().withLinearRegression()
				.withGradientDescentTraining(alpha, numberOfIterations, thetaVector).withNormalization().build();

		// Run gradient descent
		model.train(featureMatrix, yVector);

		// Predict cost of house
		double[][] features = { { 1650.0, 3.0 } };
		SimpleMatrix featureRow = new SimpleMatrix(features);
		double expectedPrice = 292748.085;
		double actualPrice = model.calculateHypothesis(featureRow);
		Assert.assertEquals(expectedPrice, actualPrice, 0.001);  
```
