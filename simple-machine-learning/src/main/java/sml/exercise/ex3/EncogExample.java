package sml.exercise.ex3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class EncogExample {

	private MLDataSet buildDataSet(List<HandWrittenDigitDataPoint> dataPoints) {
		List<MLDataPair> dataPairs = new ArrayList<>();
		for (HandWrittenDigitDataPoint dataPoint : dataPoints) {
			double[] input = dataPoint.getOriginalPixelValues();
			double[] output = new double[10];
			output[dataPoint.getValue()] = 1.0;
			dataPairs.add(new BasicMLDataPair(new BasicMLData(input), new BasicMLData(output)));
		}
		return new BasicMLDataSet(dataPairs);
	}

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
		double trainingSetPercentage = 90.0;
		for (int i = 0; i < 10; i++) {
			final int digit = i;
			int trainingSetCount = (int) (trainingSetPercentage * dataPoints.size() / 1000);
			trainingDataPoints.addAll(dataPoints.stream().filter(dp -> dp.getValue() == digit).limit(trainingSetCount)
					.collect(Collectors.toList()));
			evaluationDataPoints.addAll(dataPoints.stream().filter(dp -> dp.getValue() == digit).skip(trainingSetCount)
					.collect(Collectors.toList()));
		}

		// Set up the neural network
		System.out.println("Setting up the neural network...");
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 400));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 25));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 10));
		network.getStructure().finalizeStructure();
		network.reset();

		// Train the network
		System.out.println("Training the neural network...");
		MLDataSet trainingSet = buildDataSet(trainingDataPoints);
		// Propagation train = new ResilientPropagation(network, trainingSet);
		Propagation train = new Backpropagation(network, trainingSet);
		int epoch = 1;
		do {
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error: " + train.getError());
			epoch++;
		} while (train.getError() > 0.01);
		train.finishTraining();

		System.out.println("Analyzing results...");
		int correct = 0;
		int incorrect = 0;
		MLDataSet evaluationSet = buildDataSet(evaluationDataPoints);
		for (MLDataPair pair : evaluationSet) {
			MLData networkOutput = network.compute(pair.getInput());
			int expected = calculateHypothesis(pair.getIdeal().getData());
			int hypothesis = calculateHypothesis(networkOutput.getData());
			if (hypothesis == expected) {
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
