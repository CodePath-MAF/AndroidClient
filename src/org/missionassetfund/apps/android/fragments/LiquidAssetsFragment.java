
package org.missionassetfund.apps.android.fragments;

import java.text.NumberFormat;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

public class LiquidAssetsFragment extends Fragment {

    private PieGraph pgLiquidAssetDonutChart;
    private TextView tvRemainingAmount;
    private TextView tvSpentAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liquid_assets, container, false);

        pgLiquidAssetDonutChart = (PieGraph) view.findViewById(R.id.liquid_assets_donut_chart);
        tvRemainingAmount = (TextView) view.findViewById(R.id.tv_remaining_amount);
        tvSpentAmount = (TextView) view.findViewById(R.id.tv_spent_amount);

        setupChart();
        setupSummary();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.liquid_assets, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_transaction:
                Intent addTransactionIntent = new Intent(getActivity(),
                        AddTransactionActivity.class);
                getActivity().startActivity(addTransactionIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSummary() {
        tvRemainingAmount.setText(getCurrencyValueFormatted(234.56d));
        tvSpentAmount.setText(getCurrencyValueFormatted(-123.45d));
    }

    private void setupChart() {
        PieSlice slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.white));
        slice.setValue(5);
        pgLiquidAssetDonutChart.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.navy_blue));
        slice.setValue(5);
        pgLiquidAssetDonutChart.addSlice(slice);

        pgLiquidAssetDonutChart.setInnerCircleRatio(180);
    }

    public String getCurrencyValueFormatted(Double value) {
        NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
        return baseFormat.format(value);
    }

}
