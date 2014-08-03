
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;
import org.missionassetfund.apps.android.adapters.ChartsViewPagerAdapter;
import org.missionassetfund.apps.android.adapters.TransactionsExpandableListAdapter;
import org.missionassetfund.apps.android.models.CategoryTotal;
import org.missionassetfund.apps.android.models.Chart;
import org.missionassetfund.apps.android.models.TransactionsDashboard;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LiquidAssetsFragment extends Fragment {

    public static final int ADD_TRANSACTION_REQUEST_CODE = 1;

    private TextView tvLiquidAssetsAmount;
    private TextView tvSpentAmount;
    private TextView tvSpentTodayAmount;
    private TextView tvEmptyLiquidAssets;
    private TextView tvEmptyTransactions;
    private ProgressBar pbLoadingLiquidAssets;
    private RelativeLayout rlLiquidAssets;
    private BigDecimal mSpentToday;
    private BigDecimal mSpentThisWeek;
    private BigDecimal mLiquidAssets;
    private ExpandableListView elvTransactions;
    private TransactionsExpandableListAdapter mTransactionsAdapter;
    private ViewPager vpCharts;
    private ChartsViewPagerAdapter mChartsViewPageAdapter;
    private Chart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_liquid_assets, container, false);

        tvLiquidAssetsAmount = (TextView) view.findViewById(R.id.tvLiquidAssetsAmount);
        tvSpentAmount = (TextView) view.findViewById(R.id.tvSpentAmount);
        tvSpentTodayAmount = (TextView) view.findViewById(R.id.tvSpentTodayAmount);
        tvEmptyLiquidAssets = (TextView) view.findViewById(R.id.tvEmptyLiquidAssets);
        elvTransactions = (ExpandableListView) view.findViewById(R.id.elvTransactions);
        pbLoadingLiquidAssets = (ProgressBar) view.findViewById(R.id.pbLoadingLiquidAssets);
        rlLiquidAssets = (RelativeLayout) view.findViewById(R.id.rlLiquidAssets);
        tvEmptyTransactions = (TextView) view.findViewById(R.id.tvEmptyTransactions);
        vpCharts = (ViewPager) view.findViewById(R.id.vpCharts);

        setupData();
        return view;
    }

    private void setupData() {
        tvEmptyLiquidAssets.setVisibility(View.INVISIBLE);
        rlLiquidAssets.setVisibility(View.INVISIBLE);
        pbLoadingLiquidAssets.setVisibility(View.VISIBLE);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());

        Calendar today = Calendar.getInstance();
        params.put("day", today.get(Calendar.DAY_OF_MONTH));
        params.put("month", today.get(Calendar.MONTH) + 1); // Thanks Java...
        params.put("year", today.get(Calendar.YEAR));

        ParseCloud.callFunctionInBackground("stackedBarChartDetailView", params,
                new FunctionCallback<HashMap<String, Object>>() {

                    @Override
                    public void done(HashMap<String, Object> result, ParseException exception) {
                        if (exception != null) {
                            Log.d("LiquidAssetsFragment", "error on querying transactions",
                                    exception);
                            return;
                        }

                        final ObjectMapper mapper = new ObjectMapper();
                        mapper.setSerializationInclusion(Include.NON_NULL);

                        final TransactionsDashboard dashboard = mapper.convertValue(result,
                                TransactionsDashboard.class);
                        mChart = dashboard.getChart();

                        Boolean hasData = mChart.getHasData();

                        if (!hasData) {
                            return;
                        }

                        setupSummary(dashboard);
                        setupChart(dashboard.getChart());

                        mTransactionsAdapter = new
                                TransactionsExpandableListAdapter(
                                        dashboard.getTransactionsByDate(),
                                        getActivity());

                        elvTransactions.setAdapter(mTransactionsAdapter);
                        elvTransactions.setEmptyView(tvEmptyTransactions);
                        elvTransactions.setGroupIndicator(null);

                        for (int i = 0; i <
                        mTransactionsAdapter.getGroupCount(); i++) {
                            elvTransactions.expandGroup(i);
                        }

                        pbLoadingLiquidAssets.setVisibility(View.INVISIBLE);

                        if (isSummaryEmpty()) {
                            tvEmptyLiquidAssets.setVisibility(View.VISIBLE);
                        } else {
                            rlLiquidAssets.setVisibility(View.VISIBLE);
                        }
                    }
                });
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
                startActivityForResult(addTransactionIntent, ADD_TRANSACTION_REQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSummary(TransactionsDashboard dashboard) {
        mSpentToday = dashboard.getSpentToday();
        mSpentThisWeek = dashboard.getSpentThisWeek();
        mLiquidAssets = dashboard.getTotalCash();

        tvLiquidAssetsAmount.setText(CurrencyUtils.getCurrencyValueFormatted(mLiquidAssets));
        tvSpentAmount.setText(CurrencyUtils.getCurrencyValueFormattedAsNegative(mSpentThisWeek));
        tvSpentTodayAmount.setText(CurrencyUtils.getCurrencyValueFormattedAsNegative(mSpentToday));
    }

    private boolean isSummaryEmpty() {
        return mLiquidAssets.equals(CurrencyUtils.ZERO)
                && mSpentThisWeek.equals(CurrencyUtils.ZERO)
                && mSpentToday.equals(CurrencyUtils.ZERO);
    }

    private void setupChart(Chart chart) {
        mChartsViewPageAdapter = new ChartsViewPagerAdapter(getActivity()
                .getSupportFragmentManager());
        mChartsViewPageAdapter.setChart(chart);
        // FIXME
        // if (!mTransactionsGroupChart.isEmpty()) {
        // mChartsViewPageAdapter.setTransactionGroup(mTransactionsGroupChart.get(0));
        // }
        //
        vpCharts.setAdapter(mChartsViewPageAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setupData();
        if (resultCode == FragmentActivity.RESULT_OK && requestCode == ADD_TRANSACTION_REQUEST_CODE) {
            Toast.makeText(getActivity(), getString(R.string.parse_success_transaction_save),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void goToNextChart(int barIndex) {
        List<CategoryTotal> categoryTotals = mChart.getData().get(barIndex);
        mChartsViewPageAdapter.setCategoryTotals(categoryTotals);
        mChartsViewPageAdapter.notifyDataSetChanged();
        vpCharts.setCurrentItem(1, true);
    }

}
