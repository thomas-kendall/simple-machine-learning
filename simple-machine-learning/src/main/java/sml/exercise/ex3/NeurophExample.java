package sml.exercise.ex3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.NeuralNetworkEvent;
import org.neuroph.core.events.NeuralNetworkEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

public class NeurophExample implements NeuralNetworkEventListener {

	private int calculationNumber = 0;

	private int calculateHypothesis(double[] networkOutput) {
		int hypothesis = 0;
		for (int i = 1; i < networkOutput.length; i++) {
			if (networkOutput[i] > networkOutput[hypothesis]) {
				hypothesis = i;
			}
		}
		return hypothesis;
	}

	public void execute() throws IOException {
		System.out.println("Loading data points...");
		List<HandWrittenDigitDataPoint> dataPoints = new HandWrittenDigitDataLoader().readFile(true);
		// Split the dataset into 4000/1000 for training/evaluating
		List<HandWrittenDigitDataPoint> trainingDataPoints = new ArrayList<>();
		List<HandWrittenDigitDataPoint> evaluationDataPoints = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final int digit = i;
			trainingDataPoints.addAll(
					dataPoints.stream().filter(dp -> dp.getValue() == digit).limit(400).collect(Collectors.toList()));
			evaluationDataPoints.addAll(dataPoints.stream().filter(dp -> dp.getValue() == digit).skip(400).limit(100)
					.collect(Collectors.toList()));
		}

		// Set up the neural network
		System.out.println("Setting up the neural network...");
		MultiLayerPerceptron network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 400, 25, 10);
		network.addListener(this);
		network.randomizeWeights();

		// Train the network
		System.out.println("Training the neural network...");
		DataSet trainingSet = new DataSet(400, 10);
		for (HandWrittenDigitDataPoint dataPoint : trainingDataPoints) {
			double[] input = dataPoint.getOriginalPixelValues();
			double[] output = new double[10];
			output[dataPoint.getValue()] = 1.0;
			trainingSet.addRow(new DataSetRow(input, output));
		}
		network.learn(trainingSet);

		System.out.println("Analyzing results...");
		int correct = 0;
		int incorrect = 0;
		for (HandWrittenDigitDataPoint dataPoint : evaluationDataPoints) {
			network.setInput(dataPoint.getOriginalPixelValues());
			network.calculate();
			double[] networkOutput = network.getOutput();
			int hypothesis = calculateHypothesis(networkOutput);
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

	@Override
	public void handleNeuralNetworkEvent(NeuralNetworkEvent event) {
		if (calculationNumber % 10000 == 0) {
			System.out.println("Calculating " + calculationNumber);
		}
		calculationNumber++;
	}

}
