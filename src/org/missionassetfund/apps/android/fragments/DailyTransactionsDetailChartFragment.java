
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

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

public class DailyTransactionsDetailChartFragment extends Fragment {

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
        pgTransactionsDetailChart.removeSlices();

        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#ffffff"));
        slice.setValue(0.0001f);
        pgTransactionsDetailChart.addSlice(slice);

        for (Transaction transaction: mTransactionGroup.getTransactions()) {
            slice = new PieSlice();
            slice.setColor(Color.parseColor(transaction.getCategory().getColor()));
            slice.setValue(transaction.getAmount().floatValue());
            pgTransactionsDetailChart.addSlice(slice);
        }
        
        pgTransactionsDetailChart.setInnerCircleRatio(190);
//            pgTransactionsDetailChart.setDuration(2000);
//            pgTransactionsDetailChart.setInterpolator(new AccelerateDecelerateInterpolator());
//            pgTransactionsDetailChart.animateToGoalValues();
    }

}
