
package org.missionassetfund.apps.android.views;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class StackedBarChart {

    private XYMultipleSeriesRenderer renderer;

    public StackedBarChart(int[] colors, double xValuesEdge, float maxChartValues, float maxValue,
            String[] xTitles, Orientation orientantion, float barWidth) {
        renderer = buildBarRenderer(colors);
        setChartSettings(renderer, xValuesEdge, maxChartValues + xValuesEdge, 0, maxValue,
                Color.GRAY, Color.LTGRAY, xTitles, orientantion, barWidth);

    }

    public GraphicalView getChartView(Context context, String[] titles, List<double[]> values) {
        return ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer,
                Type.STACKED);
    }

    protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, double xMin, double xMax,
            double yMin, double yMax, int axesColor,
            int labelsColor, String[] xTitles, Orientation orientation, float width) {
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setShowLegend(false);
        renderer.setXLabels(0);

        if (xTitles != null) {
            for (int i = 0; i < xTitles.length; i++) {
                renderer.addXTextLabel(i + 1, xTitles[i]);
            }
        }

        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setPanEnabled(false, false);
        renderer.setZoomRate(1.1f);
        renderer.setBarSpacing(0.5f);
        renderer.setLabelsTextSize(24);
        renderer.setYLabels(0);
        renderer.setShowCustomTextGrid(false);
        renderer.setClickEnabled(true);
        renderer.setOrientation(orientation);
        renderer.setBarWidth(width);
    }

    protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }
}
