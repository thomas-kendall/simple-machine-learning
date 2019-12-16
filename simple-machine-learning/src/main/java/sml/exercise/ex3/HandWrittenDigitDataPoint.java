package sml.exercise.ex3;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class HandWrittenDigitDataPoint {

	private static double[][] clone(double[][] p) {
		return Arrays.stream(p).map(double[]::clone).toArray(double[][]::new);
	}

	private double[][] pixels; // 20x20

	private int value;

	public HandWrittenDigitDataPoint(double[][] pixels, int value) {
		this.pixels = clone(pixels);
		this.value = value;
	}

	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);

		for (int row = 0; row < 20; row++) {
			for (int col = 0; col < 20; col++) {
				double grayscaleFloat = 1.0 - pixels[row][col];
				int grayscaleInt = (int) (255 * grayscaleFloat);
				int rgb = grayscaleInt << 16 | grayscaleInt << 8 | grayscaleInt;
				image.setRGB(col, row, rgb);
			}
		}

		return image;
	}

	public double getPixel(int i) {
		int row = i / 20;
		int col = i % 20;
		return getPixel(row, col);
	}

	public double getPixel(int row, int col) {
		return pixels[row][col];
	}

	public double[][] getPixels() {
		return clone(pixels);
	}

	public int getValue() {
		return value;
	}

	public void scalePixels(double maxValue, double minValue) {
		for (int row = 0; row < 20; row++) {
			for (int col = 0; col < 20; col++) {
				pixels[row][col] = (pixels[row][col] - minValue) / (maxValue - minValue);
			}
		}
	}
}
