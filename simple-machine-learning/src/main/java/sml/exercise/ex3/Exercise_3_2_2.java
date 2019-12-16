package sml.exercise.ex3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import sml.core.nn.NeuralNetworkLayer;
import sml.util.ResourceLoader;

public class Exercise_3_2_2 {

	private int calculateHypothesis(SimpleMatrix networkOutput) {
		int hypothesis = 0;
		for (int i = 1; i < networkOutput.getNumElements(); i++) {
			if (networkOutput.get(i) > networkOutput.get(hypothesis)) {
				hypothesis = i;
			}
		}

		// Because the network was set up weird, we need to convert the hypothesis
		if (hypothesis == 9) {
			hypothesis = 0;
		} else {
			hypothesis++;
		}

		return hypothesis;
	}

	public void execute() throws IOException {
		System.out.println("Loading data points...");
		List<HandWrittenDigitDataPoint> dataPoints = new HandWrittenDigitDataLoader().readFile(false);

		System.out.println("Loading neural network weights...");
		SimpleMatrix theta1 = loadMatrix("exercise/exercise3/ex3theta1.txt");
		SimpleMatrix theta2 = loadMatrix("exercise/exercise3/ex3theta2.txt");

		System.out.println("Theta1 is a " + theta1.numRows() + "x" + theta1.numCols() + " matrix.");
		System.out.println("Theta2 is a " + theta2.numRows() + "x" + theta2.numCols() + " matrix.");

		System.out.println("Setting up neural network...");
		NeuralNetworkLayer inputLayer = new NeuralNetworkLayer(theta1);
		NeuralNetworkLayer hiddenLayer = new NeuralNetworkLayer(theta2);

		System.out.println("Analyzing results...");
		int correct = 0;
		int incorrect = 0;
		for (HandWrittenDigitDataPoint dataPoint : dataPoints) {
			SimpleMatrix a0 = new SimpleMatrix(400, 1);
			a0.setColumn(0, 0, dataPoint.getOriginalPixelValues());
			SimpleMatrix a1 = inputLayer.feedForward(a0);
			SimpleMatrix a2 = hiddenLayer.feedForward(a1);
			int hypothesis = calculateHypothesis(a2);
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

	private SimpleMatrix loadMatrix(String resourcePath) {
		List<double[]> rows = new ArrayList<>();

		ResourceLoader resourceLoader = new ResourceLoader();
		resourceLoader.loadAndParseResource(resourcePath, line -> {
			String[] parts = line.split(",");
			if (parts.length > 0) {
				double[] row = new double[parts.length];
				for (int i = 0; i < parts.length; i++) {
					row[i] = Double.parseDouble(parts[i]);
				}
				rows.add(row);
			}
		});

		double[][] result = new double[rows.size()][];
		for (int i = 0; i < rows.size(); i++) {
			result[i] = rows.get(i);
		}

		return new SimpleMatrix(result);
	}
}
