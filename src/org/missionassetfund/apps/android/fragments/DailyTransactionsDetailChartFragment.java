
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.GraphicalView;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.CategoryPercentageByDayAdapter;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.CategoryTotal;
import org.missionassetfund.apps.android.utils.ArrayUtils;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.views.StackedBarChart;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DailyTransactionsDetailChartFragment extends Fragment {

    private static final double X_VALUES_EDGE = 0.5;
    private static final int MAX_CHART_VALUES = 1;

    private List<CategoryTotal> mCategoryTotals;
    private String mTopChartLabel;
    private RelativeLayout rlTransactionsDetailChart;
    private TextView tvDetailChartDate;
    private CategoryPercentageByDayAdapter mCategoryPercentageByDayAdapter;
    private GridView gvDetailChartLabel;
    private GraphicalView mGraphicalView;
    private Map<Category, BigDecimal> mTransactionsPercentageByCategory;
    private BigDecimal mMaxValue;

    public void setCategoryTotals(List<CategoryTotal> categoryTotals) {
        this.mCategoryTotals = categoryTotals;
    }

    public void setTopChartLabel(String topChartLabel) {
        this.mTopChartLabel = topChartLabel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daily_transactions_detail_chart,
                container, false);

        rlTransactionsDetailChart = (RelativeLayout) view
                .findViewById(R.id.rlTransactionsDetailChart);
        tvDetailChartDate = (TextView) view.findViewById(R.id.tvDetailChartDate);
        gvDetailChartLabel = (GridView) view.findViewById(R.id.gvDetailChartLabel);

        if (mCategoryTotals != null && !mCategoryTotals.isEmpty()) {
            setupChart(view.getContext());
        }

        return view;
    }

    private void setupChart(Context context) {
        tvDetailChartDate.setText(mTopChartLabel);

        this.setupTransactionsPercentageByCategory(mCategoryTotals);
        mCategoryPercentageByDayAdapter = new CategoryPercentageByDayAdapter(this.getActivity(),
                mTransactionsPercentageByCategory);
        gvDetailChartLabel.setNumColumns(mTransactionsPercentageByCategory.size());
        gvDetailChartLabel.setAdapter(mCategoryPercentageByDayAdapter);

        int[] colors = new int[mCategoryTotals.size()];
        float maxValue = mMaxValue.floatValue();
        String[] xTitles = null;

        List<double[]> values = new ArrayList<double[]>();
        String[] titles = new String[mCategoryTotals.size()];

        for (int i = 0; i < mCategoryTotals.size(); i++) {
            CategoryTotal c = mCategoryTotals.get(i);

            double current = c.getCategoryTotal().doubleValue();
            double previous = (i == 0) ? 0 : values.get(i - 1)[0];

            colors[i] = Color.parseColor(c.getCategoryColor());
            titles[i] = c.getCategoryName();
            values.add(new double[] {
                current + previous
            });
        }

        Collections.reverse(values);
        ArrayUtils.reverse(colors);

        int[] margins = new int[] {
                getResources().getDimensionPixelOffset(R.dimen.transaction_chart_margin_top),
                getResources().getDimensionPixelOffset(R.dimen.transaction_chart_margin_left),
                getResources().getDimensionPixelOffset(R.dimen.transaction_chart_margin_bottom),
                getResources().getDimensionPixelOffset(R.dimen.transaction_chart_margin_right)
        };
        
        float labelsTextSize = getResources().getDimension(
                R.dimen.transaction_chart_labels_text_size);
        
        int xLabelColor = getResources().getColor(R.color.dashboard_cash_spent_chart_xlabel_text);

        StackedBarChart barChart = new StackedBarChart(colors, X_VALUES_EDGE, MAX_CHART_VALUES,
                maxValue, xTitles, Orientation.VERTICAL, 300f, margins, labelsTextSize, xLabelColor);
        mGraphicalView = barChart.getChartView(context, titles, values);
        rlTransactionsDetailChart.addView(mGraphicalView);
    }

    private void setupTransactionsPercentageByCategory(List<CategoryTotal> categoryTotals) {
        mTransactionsPercentageByCategory = new HashMap<Category, BigDecimal>();
        mMaxValue = CurrencyUtils.ZERO;

        for (CategoryTotal categoryTotal : categoryTotals) {
            mMaxValue = mMaxValue.add(categoryTotal.getCategoryTotal());
        }

        for (CategoryTotal categoryTotal : categoryTotals) {
            BigDecimal percentage = categoryTotal.getCategoryTotal().divide(mMaxValue, 4,
                    RoundingMode.DOWN);

            Category key = new Category();
            key.setName(categoryTotal.getCategoryName());
            key.setColor(categoryTotal.getCategoryColor());

            mTransactionsPercentageByCategory.put(key, percentage);
        }
    }

}
