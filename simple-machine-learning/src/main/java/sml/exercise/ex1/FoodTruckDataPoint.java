package sml.exercise.ex1;

public class FoodTruckDataPoint {
	private double cityPopulation;
	private double foodTruckProfit;

	public FoodTruckDataPoint(double cityPopulation, double foodTruckProfit) {
		this.cityPopulation = cityPopulation;
		this.foodTruckProfit = foodTruckProfit;
	}

	public double getCityPopulation() {
		return cityPopulation;
	}

	public double getFoodTruckProfit() {
		return foodTruckProfit;
	}

	@Override
	public String toString() {
		return "Population: " + getCityPopulation() + ", Profit: " + getFoodTruckProfit();
	}
}
