
package org.missionassetfund.apps.android.fragments;

import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.CategoryPercentageByDayAdapter;
import org.missionassetfund.apps.android.models.CategoryTotal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DailyTransactionsDetailChartFragment extends Fragment {

    private List<CategoryTotal> categoryTotals;
    private RelativeLayout rlTransactionsDetailChart;
    private TextView tvDetailChartDate;
    private CategoryPercentageByDayAdapter mCategoryPercentageByDayAdapter;
    private GridView gvDetailChartLabel;

    public List<CategoryTotal> getCategoryTotals() {
        return categoryTotals;
    }

    public void setCategoryTotals(List<CategoryTotal> categoryTotals) {
        this.categoryTotals = categoryTotals;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daily_transactions_detail_chart,
                container, false);

        rlTransactionsDetailChart = (RelativeLayout) view.findViewById(R.id.rlTransactionsDetailChart);
        tvDetailChartDate = (TextView) view.findViewById(R.id.tvDetailChartDate);
        gvDetailChartLabel = (GridView) view.findViewById(R.id.gvDetailChartLabel);
        
        setupChart();

        return view;
    }

    private void setupChart() {
//        pgTransactionsDetailChart.setPadding(1);
//        pgTransactionsDetailChart.removeSlices();
//        PieSlice slice = null;
//        
//        if (mTransactionGroup == null) {
//            return;
//        }
//        
//        tvDetailChartDate.setText(mTransactionGroup.getTransactionDateFormatted());
//        
//        mCategoryPercentageByDayAdapter = new CategoryPercentageByDayAdapter(this.getActivity(), mTransactionGroup.getTransactionGroupPercentageByCategory());
//        gvDetailChartLabel.setNumColumns(mTransactionGroup.getTransactionGroupPercentageByCategory().size());
//        gvDetailChartLabel.setAdapter(mCategoryPercentageByDayAdapter);
//        
//        for (Map.Entry<Category, BigDecimal> entry : mTransactionGroup.getTransactionGroupPercentageByCategory().entrySet()) {
//          slice = new PieSlice();
//          
//          if (entry.getKey() != null && entry.getKey().getColor() != null) {
//              slice.setColor(Color.parseColor(entry.getKey().getColor()));
//          }
//          
//          slice.setValue(1);
//          slice.setGoalValue(entry.getValue().floatValue());
//          slice.setTitle(entry.getKey().getName());
//          pgTransactionsDetailChart.addSlice(slice);
//            
//        }
//        // Hack on chart because it doesn't work with only one item
//        if (pgTransactionsDetailChart.getSlices().size() == 1) {
//            slice = new PieSlice();
//            slice.setColor(Color.parseColor("#ffffff"));
//            slice.setValue(0.001f);
//            pgTransactionsDetailChart.addSlice(slice);
//        }
//        
//        pgTransactionsDetailChart.setInnerCircleRatio(180);
//        pgTransactionsDetailChart.setDuration(2000);
//        pgTransactionsDetailChart.setInterpolator(new AccelerateDecelerateInterpolator());
//        pgTransactionsDetailChart.animateToGoalValues();
        
        
    }

}
