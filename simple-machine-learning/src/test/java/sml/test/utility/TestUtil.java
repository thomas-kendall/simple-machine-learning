package sml.test.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.ejml.simple.SimpleMatrix;

public class TestUtil {

	public static SimpleMatrix loadFileCSV(String path) throws IOException {
		// First, figure out the dimensions
		int rows = 0;
		int cols = 0;
		String line;
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(TestUtil.class.getClassLoader().getResourceAsStream(path)));
		while ((line = reader.readLine()) != null) {
			if (!line.isEmpty()) {
				rows++;
				if (cols == 0) {
					cols = line.split(",").length;
				}
			}
		}
		reader.close();

		// Set up the matrix
		double[][] data = new double[rows][cols];
		reader = new BufferedReader(new InputStreamReader(TestUtil.class.getClassLoader().getResourceAsStream(path)));
		int row = 0;
		while ((line = reader.readLine()) != null) {
			if (!line.isEmpty()) {
				String[] parts = line.split(",");
				for (int col = 0; col < cols; col++) {
					data[row][col] = Double.parseDouble(parts[col]);
				}
				row++;
			}
		}
		reader.close();

		return new SimpleMatrix(data);
	}

}
