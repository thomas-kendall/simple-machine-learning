package sml.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class XYPlotChart extends ApplicationFrame {
	private static final long serialVersionUID = -7132314608768149593L;

	private JFreeChart chart = null;
	private int datasetCount = 0;

	public XYPlotChart(String windowTitle, String chartTitle, String xAxisLabel, String yAxisLabel) {
		super(windowTitle);
		chart = ChartFactory.createScatterPlot(chartTitle, xAxisLabel, yAxisLabel, null, PlotOrientation.VERTICAL, true,
				true, false);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 500));
		setContentPane(chartPanel);
	}

	public XYPlotChart addDataset(String seriesKey, boolean line, double[] xSeries, double[] ySeries) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries(seriesKey, new double[][] { xSeries, ySeries });

		int datasetIndex = datasetCount++;
		XYPlot plot = chart.getXYPlot();
		plot.setDataset(datasetIndex, dataset);
		XYItemRenderer renderer = line ? new StandardXYItemRenderer() : new XYShapeRenderer();
		plot.setRenderer(datasetIndex, renderer);

		return this;
	}

	public XYPlotChart render() {
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);

		return this;
	}
}
