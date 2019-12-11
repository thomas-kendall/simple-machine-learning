package sml.exercise.ex2;

import java.util.ArrayList;
import java.util.List;

import sml.charting.XYPlotChart;
import sml.util.ResourceLoader;

public class Exercise_2_1_1 {

	public void execute() {
		List<ExamScoresDataPoint> dataPoints = readFile();
		for (ExamScoresDataPoint dataPoint : dataPoints) {
			System.out.println(dataPoint);
		}

		double[] xSeriesAdmitted = dataPoints.stream().filter(dataPoint -> dataPoint.isAdmitted())
				.mapToDouble(dataPoint -> dataPoint.getExam1Score()).toArray();
		double[] ySeriesAdmitted = dataPoints.stream().filter(dataPoint -> dataPoint.isAdmitted())
				.mapToDouble(dataPoint -> dataPoint.getExam2Score()).toArray();

		double[] xSeriesDenied = dataPoints.stream().filter(dataPoint -> !dataPoint.isAdmitted())
				.mapToDouble(dataPoint -> dataPoint.getExam1Score()).toArray();
		double[] ySeriesDenied = dataPoints.stream().filter(dataPoint -> !dataPoint.isAdmitted())
				.mapToDouble(dataPoint -> dataPoint.getExam2Score()).toArray();

		XYPlotChart chart = new XYPlotChart("Exercise 2 - 1.1", "Admission via Exam Scores", "Exam 1 score",
				"Exam 2 score").addDataset("Admitted", false, xSeriesAdmitted, ySeriesAdmitted)
						.addDataset("Not Admitted", false, xSeriesDenied, ySeriesDenied).render();
	}

	private List<ExamScoresDataPoint> readFile() {
		List<ExamScoresDataPoint> dataPoints = new ArrayList<>();

		ResourceLoader resourceLoader = new ResourceLoader();
		resourceLoader.loadAndParseResource("exercise/exercise2/ex2data1.txt", line -> {
			String[] parts = line.split(",");
			if (parts.length == 3) {
				dataPoints.add(new ExamScoresDataPoint(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),
						Integer.parseInt(parts[2]) == 1));
			}
		});

		return dataPoints;
	}
}
