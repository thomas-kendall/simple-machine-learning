package sml.exercise.ex1;

import java.util.ArrayList;
import java.util.List;

import sml.charting.XYPlotChart;
import sml.util.ResourceLoader;

public class Exercise_1_2_1 {

	public void execute() {
		List<FoodTruckDataPoint> dataPoints = readFile();
		for (FoodTruckDataPoint dataPoint : dataPoints) {
			System.out.println(dataPoint);
		}

		double[] xSeries = dataPoints.stream().mapToDouble(dataPoint -> dataPoint.getCityPopulation()).toArray();
		double[] ySeries = dataPoints.stream().mapToDouble(dataPoint -> dataPoint.getFoodTruckProfit()).toArray();

		XYPlotChart chart = new XYPlotChart("Exercise 2.1", "Food truck profit vs Population", "Population of City in 10,000s",
				"Profit in $10,000s").addDataset("Profit", false, xSeries, ySeries).render();
	}

	private List<FoodTruckDataPoint> readFile() {
		List<FoodTruckDataPoint> dataPoints = new ArrayList<>();

		ResourceLoader resourceLoader = new ResourceLoader();
		resourceLoader.loadAndParseResource("exercise/exercise1/ex1data1.txt", line -> {
			String[] parts = line.split(",");
			if (parts.length == 2) {
				dataPoints.add(new FoodTruckDataPoint(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])));
			}
		});

		return dataPoints;
	}
}
