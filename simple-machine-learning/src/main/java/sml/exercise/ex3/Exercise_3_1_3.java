package sml.exercise.ex3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.ejml.simple.SimpleMatrix;

import sml.core.IRegressionModel;
import sml.core.builders.RegressionModelBuilder;

public class Exercise_3_1_3 {

	private SimpleMatrix buildFeatureMatrix(List<HandWrittenDigitDataPoint> dataPoints) {
		SimpleMatrix featureMatrix = new SimpleMatrix(dataPoints.size(), 400);
		for (int row = 0; row < dataPoints.size(); row++) {
			for (int i = 0; i < 400; i++) {
				featureMatrix.set(row, i, dataPoints.get(row).getPixel(i));
			}
		}
		return featureMatrix;
	}

	private SimpleMatrix buildFeatureRow(HandWrittenDigitDataPoint dataPoint) {
		SimpleMatrix featureRow = new SimpleMatrix(1, 400);
		for (int i = 0; i < 400; i++) {
			featureRow.set(0, i, dataPoint.getPixel(i));
		}
		return featureRow;
	}

	private SimpleMatrix buildYVector(List<HandWrittenDigitDataPoint> dataPoints, int digitForClassification) {
		SimpleMatrix yVector = new SimpleMatrix(dataPoints.size(), 1);
		for (int row = 0; row < dataPoints.size(); row++) {
			yVector.set(row, 0, dataPoints.get(row).getValue() == digitForClassification ? 1 : 0);
		}
		return yVector;
	}

	private int calculateHypothesis(Map<Integer, IRegressionModel> classificationMap, SimpleMatrix featureRow) {
		int hypothesis = 0;
		double hypothesisProbability = 0.0;

		for (int key : classificationMap.keySet()) {
			double probability = classificationMap.get(key).calculateHypothesis(featureRow);
			if (probability > hypothesisProbability) {
				hypothesisProbability = probability;
				hypothesis = key;
			}
		}

		return hypothesis;
	}

	public void execute() throws IOException {
		System.out.println("Loading data points...");
		List<HandWrittenDigitDataPoint> dataPoints = new HandWrittenDigitDataLoader().readFile();

		System.out.println("Setting up classification models...");
		// Split the dataset into 400/100 for training/evaluating
		List<HandWrittenDigitDataPoint> trainingDataPoints = new ArrayList<>();
		List<HandWrittenDigitDataPoint> evaluationDataPoints = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final int digit = i;
			trainingDataPoints.addAll(
					dataPoints.stream().filter(dp -> dp.getValue() == digit).limit(400).collect(Collectors.toList()));
			evaluationDataPoints.addAll(dataPoints.stream().filter(dp -> dp.getValue() == digit).skip(400).limit(100)
					.collect(Collectors.toList()));
		}

		// A regression model for each of our digits
		System.out.println("Setting up classification map...");
		Map<Integer, IRegressionModel> classificationMap = new HashMap<>();
		double alpha = 0.01;
		int numberOfIterations = 50;
		SimpleMatrix featureMatrix = buildFeatureMatrix(trainingDataPoints);
		for (int i = 0; i < 10; i++) {
			IRegressionModel regressionModel = new RegressionModelBuilder().withLogisticRegression()
					.withoutNormalization().withGradientDescentTraining(alpha, numberOfIterations).build();
			classificationMap.put(i, regressionModel);
		}

		// Set up multithreading
		ExecutorService executorService = Executors.newFixedThreadPool(classificationMap.size());
		List<Callable<Void>> tasks = new ArrayList<>();
		for (int key : classificationMap.keySet()) {
			tasks.add(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					System.out.println("Training classification model " + key + "...");
					SimpleMatrix yVector = buildYVector(trainingDataPoints, key);
					classificationMap.get(key).train(featureMatrix, yVector);
					return null;
				};
			});
		}
		try {
			executorService.invokeAll(tasks);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}

		System.out.println("Analyzing results...");
		int correct = 0;
		int incorrect = 0;
		for (HandWrittenDigitDataPoint dataPoint : evaluationDataPoints) {
			int hypothesis = calculateHypothesis(classificationMap, buildFeatureRow(dataPoint));
			if (hypothesis == dataPoint.getValue()) {
				correct++;
			} else {
				incorrect++;
			}
		}
		double percentageCorrect = 100.0 * correct / (correct + incorrect);
		System.out.println("Percentage correct: " + percentageCorrect);

		System.out.println("Done.");
	}
}
