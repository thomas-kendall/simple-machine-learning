package sml.exercise.ex3;

import java.util.ArrayList;
import java.util.List;

import sml.util.ResourceLoader;

public class HandWrittenDigitDataLoader {
	private double minValue = 1000;
	private double maxValue = -1000;

	public List<HandWrittenDigitDataPoint> readFile(boolean applyNormalization) {
		List<HandWrittenDigitDataPoint> dataPoints = new ArrayList<>();

		ResourceLoader resourceLoader = new ResourceLoader();
		resourceLoader.loadAndParseResource("exercise/exercise3/ex3data1.txt", line -> {
			String[] parts = line.split(",");
			if (parts.length == 401) {
				double[] pixels = new double[400];
				for (int i = 0; i < 400; i++) {
					pixels[i] = Double.parseDouble(parts[i]);

					if (pixels[i] < minValue)
						minValue = pixels[i];
					if (pixels[i] > maxValue)
						maxValue = pixels[i];

				}
				int value = Integer.parseInt(parts[400]);
				if (value == 10)
					value = 0;
				dataPoints.add(new HandWrittenDigitDataPoint(transformPixels(pixels), value));
			}
		});

		// Apply scaling to normalize
		if (applyNormalization) {
			for (HandWrittenDigitDataPoint dataPoint : dataPoints) {
				dataPoint.scalePixels(maxValue, minValue);
			}
		}

		return dataPoints;
	}

	private double[][] transformPixels(double[] pixelsFromFile) {
		double[][] pixels = new double[20][20];

		for (int i = 0; i < 400; i++) {
			// Determine which row/col this pixel represents
			int row = i % 20;
			int col = i / 20;

			// Store in the field
			pixels[row][col] = pixelsFromFile[i];
		}

		return pixels;
	}

}
