
package org.missionassetfund.apps.android.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.TransactionGroup;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.ExtendedBar;
import com.echo.holographlibrary.BarGraph.OnBarClickedListener;

public class DailyTransactionsChartFragment extends Fragment {

    private List<TransactionGroup> mTransactionGroups;
    private BarGraph bgDailyTransactionsChart;
    private OnTransactionGroupClickedListener listener;
    
    public interface OnTransactionGroupClickedListener {
        public void onBarClicked(TransactionGroup transactionGroup);
    }

    public void setTransactionGroups(List<TransactionGroup> transactionGroups) {
        this.mTransactionGroups = transactionGroups;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daily_transactions_chart, container,
                false);

        bgDailyTransactionsChart = (BarGraph) view.findViewById(R.id.bgDailyTransactionsChart);
        bgDailyTransactionsChart.setOnBarClickedListener(new OnBarClickedListener() {
            
            @Override
            public void onClick(int index) {
                ExtendedBar bar = (ExtendedBar) bgDailyTransactionsChart.getBars().get(index);
                listener.onBarClicked((TransactionGroup) bar.getObjectHolder());
            }
        });
        setupChart();
        return view;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnTransactionGroupClickedListener) {
            listener = (OnTransactionGroupClickedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement DailyTransactionsChartFragment.OnTransactionGroupClickedListener");
        }
    }

    private void setupChart() {
        ArrayList<Bar> points = new ArrayList<Bar>();
        ArrayList<TransactionGroup> reversedList = new ArrayList<TransactionGroup>(
                mTransactionGroups);
        Collections.reverse(reversedList);

        for (TransactionGroup tg : reversedList) {
            ExtendedBar d = new ExtendedBar();
            // TODO
            d.setColor(Color.parseColor("#E82C0C"));
            d.setName(tg.getRelativeDate().toString());
            d.setValue(tg.getAmount().floatValue());
            d.setObjectHolder(tg);

            points.add(d);
        }

        bgDailyTransactionsChart.setBars(points);
    }

}
