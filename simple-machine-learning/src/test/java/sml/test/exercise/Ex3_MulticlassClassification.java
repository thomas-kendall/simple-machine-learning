package sml.test.exercise;

import java.io.IOException;

import org.ejml.simple.SimpleMatrix;
import org.junit.Assert;
import org.junit.Test;

import sml.core.utility.SmlUtil;
import sml.test.utility.TestUtil;

public class Ex3_MulticlassClassification {
	@Test
	public void exercise_3_1_1_loadDigitDataCorrectly() throws IOException {
		// Size of house in square feet, House Price
		SimpleMatrix matrix = TestUtil.loadFileCSV("ex3data1.txt");
		SimpleMatrix featureMatrix = SmlUtil.prependOnesVector(matrix.cols(0, matrix.numCols() - 1));
		SimpleMatrix yVector = matrix.cols(matrix.numCols() - 1, matrix.numCols());

		Assert.assertEquals(401, featureMatrix.numCols());
		Assert.assertEquals(5000, featureMatrix.numRows());
		Assert.assertEquals(1, yVector.numCols());
		Assert.assertEquals(5000, yVector.numRows());
	}

}
