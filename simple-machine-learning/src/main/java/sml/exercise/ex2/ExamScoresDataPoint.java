package sml.exercise.ex2;

public class ExamScoresDataPoint {
	private double exam1Score;
	private double exam2Score;
	private boolean admitted;

	public ExamScoresDataPoint(double exam1Score, double exam2Score, boolean admitted) {
		this.exam1Score = exam1Score;
		this.exam2Score = exam2Score;
		this.admitted = admitted;
	}

	public double getExam1Score() {
		return exam1Score;
	}

	public double getExam2Score() {
		return exam2Score;
	}

	public boolean isAdmitted() {
		return admitted;
	}

	@Override
	public String toString() {
		return "Score 1: " + getExam1Score() + ", Score 2: " + getExam2Score() + ", Admitted: " + isAdmitted();
	}
}
