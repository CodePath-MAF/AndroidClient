
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;
import org.missionassetfund.apps.android.adapters.ChartsViewPagerAdapter;
import org.missionassetfund.apps.android.adapters.TransactionsExpandableListAdapter;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.TransactionGroup;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.utils.MAFDateUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
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

import com.parse.FindCallback;
import com.parse.ParseQuery;

public class LiquidAssetsFragment extends Fragment {

    public static final int ADD_TRANSACTION_REQUEST_CODE = 1;

    private TextView tvLiquidAssetsAmount;
    private TextView tvSpentAmount;
    private TextView tvSpentTodayAmount;
    private TextView tvEmptyLiquidAssets;
    private TextView tvEmptyTransactions;
    private ProgressBar pbLoadingLiquidAssets;
    private RelativeLayout rlLiquidAssets;
    private List<TransactionGroup> mTransactionsGroup;
    private List<TransactionGroup> mTransactionsGroupChart;
    private BigDecimal mSpentToday;
    private BigDecimal mSpentThisWeek;
    private BigDecimal mLiquidAssets;
    private ExpandableListView elvTransactions;
    private TransactionsExpandableListAdapter mTransactionsAdapter;
    private ViewPager vpCharts;
    private ChartsViewPagerAdapter mChartsViewPageAdapter;

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

        ParseQuery<Transaction> query = ParseQuery.getQuery("Transaction");
        query.whereEqualTo("user", User.getCurrentUser());
        query.orderByDescending("transactionDate");
        query.include("category");

        query.findInBackground(new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> transactions, com.parse.ParseException exception) {
                if (exception != null) {
                    Log.d("LiquidAssetsFragment", "error on querying transactions", exception);
                    return;
                }

                setupTransactions(transactions);
                setupSummary();
                setupChart();

                mTransactionsAdapter = new TransactionsExpandableListAdapter(mTransactionsGroup,
                        getActivity());

                elvTransactions.setAdapter(mTransactionsAdapter);
                elvTransactions.setEmptyView(tvEmptyTransactions);
                elvTransactions.setGroupIndicator(null);

                for (int i = 0; i < mTransactionsAdapter.getGroupCount(); i++) {
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

    private void setupTransactions(List<Transaction> transactions) {
        mTransactionsGroup = new ArrayList<TransactionGroup>();
        mTransactionsGroupChart = new ArrayList<TransactionGroup>();
        mSpentToday = CurrencyUtils.ZERO;
        mSpentThisWeek = CurrencyUtils.ZERO;
        mLiquidAssets = CurrencyUtils.ZERO;

        TransactionGroup tg = null;
        TransactionGroup tgChart = null;
        int index = -1;
        int indexChart = -1;

        for (Transaction t : transactions) {
            // Set transactions for ListView
            tg = new TransactionGroup(t.getTransactionDate(), new ArrayList<Transaction>());
            tgChart = new TransactionGroup(t.getTransactionDate(), new ArrayList<Transaction>());
            index = mTransactionsGroup.indexOf(tg);
            indexChart = mTransactionsGroupChart.indexOf(tg);

            if (index == -1) {
                tg.getTransactions().add(t);
                mTransactionsGroup.add(tg);
            } else {
                mTransactionsGroup.get(index).getTransactions().add(t);
            }

            if (indexChart == -1 && t.isCredit()) {
                tgChart.getTransactions().add(t);
                mTransactionsGroupChart.add(tgChart);
            } else if (t.isCredit()) {
                mTransactionsGroupChart.get(indexChart).getTransactions().add(t);
            }

            if (t.isCredit()) {
                if (DateUtils.isToday(t.getTransactionDate().getTime())) {
                    mSpentToday = mSpentToday.add(BigDecimal.valueOf(t.getAmount()));
                }

                if (MAFDateUtils.isSameWeek(t.getTransactionDate())) {
                    mSpentThisWeek = mSpentThisWeek.add(BigDecimal.valueOf(t.getAmount()));
                }

                mLiquidAssets = mLiquidAssets.subtract(BigDecimal.valueOf(t.getAmount()));
            } else if (t.isDebit()) {
                mLiquidAssets = mLiquidAssets.add(BigDecimal.valueOf(t.getAmount()));
            }
        }

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

    private void setupSummary() {
        tvLiquidAssetsAmount.setText(CurrencyUtils.getCurrencyValueFormatted(mLiquidAssets));
        tvSpentAmount.setText(CurrencyUtils.getCurrencyValueFormattedAsNegative(mSpentThisWeek));
        tvSpentTodayAmount.setText(CurrencyUtils.getCurrencyValueFormattedAsNegative(mSpentToday));
    }

    private boolean isSummaryEmpty() {
        return mLiquidAssets.equals(CurrencyUtils.ZERO)
                && mSpentThisWeek.equals(CurrencyUtils.ZERO)
                && mSpentToday.equals(CurrencyUtils.ZERO);
    }

    private void setupChart() {
        mChartsViewPageAdapter = new ChartsViewPagerAdapter(getActivity()
                .getSupportFragmentManager());
        mChartsViewPageAdapter.setTransactionGroups(mTransactionsGroupChart);

        if (!mTransactionsGroupChart.isEmpty()) {
            mChartsViewPageAdapter.setTransactionGroup(mTransactionsGroupChart.get(0));
        }

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

    public void goToNextChart(TransactionGroup transactionGroup) {
        mChartsViewPageAdapter.setTransactionGroup(transactionGroup);
        mChartsViewPageAdapter.notifyDataSetChanged();
        vpCharts.setCurrentItem(1, true);
    }

}
