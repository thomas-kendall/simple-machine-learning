package sml.core.algorithms.training;

import org.ejml.simple.SimpleMatrix;

public interface ITrainingAlgorithm {

	SimpleMatrix train(SimpleMatrix featureMatrix, SimpleMatrix yVector);
}
