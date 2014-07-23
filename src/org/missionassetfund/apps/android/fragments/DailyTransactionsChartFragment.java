
package org.missionassetfund.apps.android.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.TransactionGroup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.BarGraph.OnBarClickedListener;
import com.echo.holographlibrary.ExtendedBar;

public class DailyTransactionsChartFragment extends Fragment {

    private static final int AVERAGE_DAILY_SPEND = 50;
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
            throw new ClassCastException(
                    activity.toString()
                            + " must implement DailyTransactionsChartFragment.OnTransactionGroupClickedListener");
        }
    }

    private void setupChart() {
        if (mTransactionGroups.isEmpty()) {
            return;
        }

        ArrayList<Bar> points = new ArrayList<Bar>();
        ArrayList<TransactionGroup> reversedList = new ArrayList<TransactionGroup>(
                mTransactionGroups);

        // Pre-populate 7 day bars with 0 values
        SimpleDateFormat sdf = new SimpleDateFormat("EEE (MM/dd)", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -6);
        for (int i = 0; i < 7; i++) {
            Log.d("DEBUG", sdf.format(cal.getTime()));
            ExtendedBar bar = new ExtendedBar();
            bar.setColor(this.getResources().getColor(R.color.bar_chart_color));
            bar.setLabelColor(this.getResources().getColor(R.color.bar_chart_label));
            bar.setName(sdf.format(cal.getTime()));
            bar.setValue(0f);
            points.add(i, bar);
            cal.add(Calendar.DATE, 1);
        }

        int pos = 6;

        for (TransactionGroup tg : reversedList) {
            int i = pos;
            for (; i > 0; i--) {
                ExtendedBar bar = (ExtendedBar) points.get(i);
                if (tg.getTransactionDateFormatted().equals(bar.getName())) {
                    bar.setValue(tg.getSpentAmount().floatValue());
                    bar.setObjectHolder(tg);
                    points.set(i, bar);
                    i--;
                    break;
                }
            }

            if (i > 0) {
                pos = i;
            } else {
                break;
            }
        }

        bgDailyTransactionsChart.setBars(points);
        bgDailyTransactionsChart.setShowPopup(false);
        bgDailyTransactionsChart.setShowAxisLabel(true);
        bgDailyTransactionsChart.setValueStringPrecision(2);
    }

}
