
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.TransactionGroup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

public class DailyTransactionsDetailChartFragment extends Fragment {

    private static final int AVERAGE_DAILY_SPEND = 50;
    private TransactionGroup mTransactionGroup;
    private PieGraph pgTransactionsDetailChart;

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.mTransactionGroup = transactionGroup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daily_transactions_detail_chart,
                container, false);

        pgTransactionsDetailChart = (PieGraph) view.findViewById(R.id.pgTransactionsDetailChart);
        setupChart();

        return view;
    }

    private void setupChart() {
        pgTransactionsDetailChart.setPadding(1);
        pgTransactionsDetailChart.removeSlices();
        PieSlice slice = null;

        for (Transaction transaction: mTransactionGroup.getTransactions()) {
            if (transaction.isCredit()) {
                slice = new PieSlice();
                
                if (transaction.getCategory() != null && transaction.getCategory().getColor() != null) {
                    slice.setColor(Color.parseColor(transaction.getCategory().getColor()));
                }
                
                slice.setValue(AVERAGE_DAILY_SPEND);
                slice.setGoalValue(transaction.getAmount().floatValue());
                slice.setTitle(transaction.getCategory().getName());
                pgTransactionsDetailChart.addSlice(slice);
            }
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
