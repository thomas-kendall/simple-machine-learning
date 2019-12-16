package sml.core.nn;

import org.ejml.simple.SimpleMatrix;

import sml.core.utility.SmlUtil;

public class NeuralNetworkLayer {
	private SimpleMatrix theta; // weights

	public NeuralNetworkLayer(SimpleMatrix initialWeights) {
		theta = initialWeights.copy();
	}

	public SimpleMatrix feedForward(SimpleMatrix inputVector) {
		// Apply the bias term
		SimpleMatrix input = new SimpleMatrix(inputVector.numRows() + 1, 1);
		input.set(0, 1.0);
		input.setColumn(0, 1, SmlUtil.getVectorData(inputVector));

		// Calculate the result
		SimpleMatrix z = theta.mult(input);
		SimpleMatrix a = SmlUtil.sigmoid(z);

		return a;
	}
}
