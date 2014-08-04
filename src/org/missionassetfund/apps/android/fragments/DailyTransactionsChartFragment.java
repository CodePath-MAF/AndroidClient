
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.CategoryTotal;
import org.missionassetfund.apps.android.models.Chart;
import org.missionassetfund.apps.android.models.dao.CategoryDao;
import org.missionassetfund.apps.android.views.StackedBarChart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class DailyTransactionsChartFragment extends Fragment {

    private static final double X_VALUES_EDGE = 0.5;
    private static final int MAX_CHART_VALUES = 7;
    private OnTransactionsBarClickedListener listener;
    private Chart mChart;
    private GraphicalView mGraphicalView;

    public interface OnTransactionsBarClickedListener {
        public void onBarClicked(int barIndex);
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
        if (activity instanceof OnTransactionsBarClickedListener) {
            listener = (OnTransactionsBarClickedListener) activity;
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

        int[] margins = new int[] {
                getResources().getDimensionPixelOffset(R.dimen.transaction_chart_margin_top),
                getResources().getDimensionPixelOffset(R.dimen.transaction_chart_margin_left),
                getResources().getDimensionPixelOffset(R.dimen.transaction_chart_margin_bottom),
                getResources().getDimensionPixelOffset(R.dimen.transaction_chart_margin_right)
        };

        StackedBarChart barChart = new StackedBarChart(categoriesColors, X_VALUES_EDGE,
                MAX_CHART_VALUES, maxValue.floatValue(), xTitles, Orientation.HORIZONTAL, 50f,
                margins);
        mGraphicalView = barChart.getChartView(context, categoriesTitles, values);

        mGraphicalView.setOnClickListener(chartClickListener);

        rlStackedBarChart.addView(mGraphicalView);
    }

    public void setChart(Chart chart) {
        this.mChart = chart;
    }

    private OnClickListener chartClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            SeriesSelection seriesSelection = mGraphicalView.getCurrentSeriesAndPoint();
            listener.onBarClicked(seriesSelection == null ? -1 : seriesSelection.getPointIndex());
        }
    };

}
