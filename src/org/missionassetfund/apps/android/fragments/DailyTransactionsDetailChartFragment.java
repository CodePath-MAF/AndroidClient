
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.util.Map;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.CategoryPercentageByDayAdapter;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.TransactionGroup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.GridView;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

public class DailyTransactionsDetailChartFragment extends Fragment {

    private TransactionGroup mTransactionGroup;
    private PieGraph pgTransactionsDetailChart;
    private TextView tvDetailChartDate;
    private CategoryPercentageByDayAdapter mCategoryPercentageByDayAdapter;
    private GridView gvDetailChartLabel;

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.mTransactionGroup = transactionGroup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daily_transactions_detail_chart,
                container, false);

        pgTransactionsDetailChart = (PieGraph) view.findViewById(R.id.pgTransactionsDetailChart);
        tvDetailChartDate = (TextView) view.findViewById(R.id.tvDetailChartDate);
        gvDetailChartLabel = (GridView) view.findViewById(R.id.gvDetailChartLabel);
        
        setupChart();

        return view;
    }

    private void setupChart() {
        pgTransactionsDetailChart.setPadding(1);
        pgTransactionsDetailChart.removeSlices();
        PieSlice slice = null;
        
        if (mTransactionGroup == null) {
            return;
        }
        
        tvDetailChartDate.setText(mTransactionGroup.getTransactionDateFormatted());
        
        mCategoryPercentageByDayAdapter = new CategoryPercentageByDayAdapter(this.getActivity(), mTransactionGroup.getTransactionGroupPercentageByCategory());
        gvDetailChartLabel.setNumColumns(mTransactionGroup.getTransactionGroupPercentageByCategory().size());
        gvDetailChartLabel.setAdapter(mCategoryPercentageByDayAdapter);
        
        for (Map.Entry<Category, BigDecimal> entry : mTransactionGroup.getTransactionGroupPercentageByCategory().entrySet()) {
          slice = new PieSlice();
          
          if (entry.getKey() != null && entry.getKey().getColor() != null) {
              slice.setColor(Color.parseColor(entry.getKey().getColor()));
          }
          
          slice.setValue(1);
          slice.setGoalValue(entry.getValue().floatValue());
          slice.setTitle(entry.getKey().getName());
          pgTransactionsDetailChart.addSlice(slice);
            
        }
        // Hack on chart because it doesn't work with only one item
        if (pgTransactionsDetailChart.getSlices().size() == 1) {
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#ffffff"));
            slice.setValue(0.001f);
            pgTransactionsDetailChart.addSlice(slice);
        }
        
        pgTransactionsDetailChart.setInnerCircleRatio(180);
        pgTransactionsDetailChart.setDuration(2000);
        pgTransactionsDetailChart.setInterpolator(new AccelerateDecelerateInterpolator());
        pgTransactionsDetailChart.animateToGoalValues();
    }

}
