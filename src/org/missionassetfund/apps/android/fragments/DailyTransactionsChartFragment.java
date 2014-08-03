
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.CategoryTotal;
import org.missionassetfund.apps.android.models.Chart;
import org.missionassetfund.apps.android.models.TransactionGroup;
import org.missionassetfund.apps.android.models.dao.CategoryDao;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class DailyTransactionsChartFragment extends Fragment {

    private static final double X_VALUES_EDGE = 0.5;
    private static final int MAX_CHART_VALUES = 7;
    // FIXME
    private OnTransactionGroupClickedListener listener;
    private Chart mChart;

    public interface OnTransactionGroupClickedListener {
        public void onBarClicked(TransactionGroup transactionGroup);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daily_transactions_chart, container,
                false);

        final RelativeLayout rlStackedBarChart = (RelativeLayout) view
                .findViewById(R.id.rlStackedBarChart);
                        
        Boolean hasData = mChart.getHasData();

        if (!hasData) {
            return view;
        }

        BigDecimal maxValue = mChart.getMaxValue();
                
        List<List<CategoryTotal>> data = mChart.getData();
        List<String> xLabels = mChart.getxLabels();

        setupChart(view.getContext(), rlStackedBarChart,
                maxValue, data, xLabels);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnTransactionGroupClickedListener) {
            listener = (OnTransactionGroupClickedListener) activity;
        } else {
            throw new ClassCastException(
                    activity.toString()
                            + " must implement DailyTransactionsChartFragment.OnTransactionGroupClickedListener");
        }
    }

    private void setupChart(Context context, RelativeLayout rlStackedBarChart, BigDecimal maxValue,
            List<List<CategoryTotal>> data, List<String> xLabels) {
        List<Category> categories = new CategoryDao().getAll();
        List<double[]> values = new ArrayList<double[]>();

        for (int i = 0; i < categories.size(); i++) {
            values.add(new double[MAX_CHART_VALUES]);
        }

        for (int i = 0; i < categories.size(); i++) {
            Category c = categories.get(i);

            for (int j = 0; j < data.size(); j++) {
                List<CategoryTotal> categoriesByDay = data.get(j);

                for (CategoryTotal map : categoriesByDay) {
                    String categoryName = map.getCategoryName();

                    if (categoryName.equals(c.getName())) {
                        double[] v = values.get(i);
                        BigDecimal total = map.getCategoryTotal();
                        v[j] = total.doubleValue();
                    }
                }

            }
        }

        for (int i = 0; i < values.size(); i++) {
            double[] previous = (i == 0) ? new double[MAX_CHART_VALUES] : values.get(i - 1);
            double[] current = values.get(i);

            for (int j = 0; j < current.length; j++) {
                current[j] = current[j] + previous[j];
            }
        }

        Collections.reverse(values);
        Collections.reverse(categories);

        String[] categoriesTitles = new String[categories.size()];
        int[] categoriesColors = new int[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            categoriesColors[i] = Color.parseColor(category.getColor());
            categoriesTitles[i] = category.getName();
        }

        String[] xTitles = xLabels.toArray(new String[xLabels.size()]);

        XYMultipleSeriesRenderer renderer = buildBarRenderer(categoriesColors);
        setChartSettings(renderer, X_VALUES_EDGE,
                MAX_CHART_VALUES + X_VALUES_EDGE, 0, maxValue.floatValue(), Color.GRAY, Color.LTGRAY, xTitles);
        
        GraphicalView graphicalView = ChartFactory.getBarChartView(context,
                buildBarDataset(categoriesTitles, values), renderer,
                Type.STACKED);
        
        rlStackedBarChart.addView(graphicalView);
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
            int labelsColor, String[] xTitles) {
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setShowLegend(false);
        renderer.setXLabels(0);

        for (int i = 0; i < xTitles.length; i++) {
            renderer.addXTextLabel(i + 1, xTitles[i]);
        }

        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setPanEnabled(false, false);
        renderer.setZoomRate(1.1f);
        renderer.setBarSpacing(0.5f);
        renderer.setLabelsTextSize(24);
        renderer.setYLabels(0);
        renderer.setShowCustomTextGrid(true);
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

    public void setChart(Chart chart) {
        this.mChart = chart;
    }

}
