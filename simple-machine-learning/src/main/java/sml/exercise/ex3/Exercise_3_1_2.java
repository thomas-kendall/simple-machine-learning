package sml.exercise.ex3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class Exercise_3_1_2 {

	private void copyImage(BufferedImage sourceImage, BufferedImage destinationImage, int destinationX,
			int destinationY) {
		for (int y = 0; y < sourceImage.getHeight(); y++) {
			for (int x = 0; x < sourceImage.getWidth(); x++) {
				destinationImage.setRGB(destinationX + x, destinationY + y, sourceImage.getRGB(x, y));
			}
		}
	}

	public void execute() throws IOException {
		System.out.println("Loading data points...");
		List<HandWrittenDigitDataPoint> dataPoints = new HandWrittenDigitDataLoader().readFile(true);

		System.out.println("Generating output...");
		int rows = 10;
		int cols = 200;
		BufferedImage outputImage = new BufferedImage(20 * cols, 20 * rows, BufferedImage.TYPE_INT_RGB);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int index = 500 * row + col;
				BufferedImage image = dataPoints.get(index).getImage();
				int destinationX = col * 20;
				int destinationY = row * 20;
				copyImage(image, outputImage, destinationX, destinationY);
			}
		}

		System.out.println("Saving output file...");
		String outputDirectory = "target/output/exercises/ex3/";
		File directory = new File(outputDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File outputFile = new File(outputDirectory + "samples.jpg");
		ImageIO.write(outputImage, "jpg", outputFile);
		System.out.println("Done.");
	}
}
