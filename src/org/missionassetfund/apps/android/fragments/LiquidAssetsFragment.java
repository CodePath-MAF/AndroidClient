
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;
import org.missionassetfund.apps.android.adapters.TransactionsExpandableListAdapter;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.Transaction.TransactionType;
import org.missionassetfund.apps.android.models.TransactionGroup;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.utils.DateUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LiquidAssetsFragment extends Fragment {

    public static final int ADD_TRANSACTION_REQUEST_CODE = 1;

    private PieGraph pgLiquidAssetDonutChart;
    private TextView tvLiquidAssetsAmount;
    private TextView tvSpentAmount;
    private TextView tvSpentTodayAmount;
    private List<TransactionGroup> mTransactionsGroup;
    private BigDecimal mSpentToday;
    private BigDecimal mSpentThisWeek;
    private BigDecimal mLiquidAssets;
    private ExpandableListView elvTransactions;
    private TransactionsExpandableListAdapter mTransactionsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_liquid_assets, container, false);

        pgLiquidAssetDonutChart = (PieGraph) view.findViewById(R.id.pgLiquidAssetDonutChart);
        tvLiquidAssetsAmount = (TextView) view.findViewById(R.id.tvLiquidAssetsAmount);
        tvSpentAmount = (TextView) view.findViewById(R.id.tvSpentAmount);
        tvSpentTodayAmount = (TextView) view.findViewById(R.id.tvSpentTodayAmount);
        elvTransactions = (ExpandableListView) view.findViewById(R.id.elvTransactions);

        ParseQuery<Transaction> query = ParseQuery.getQuery("Transaction");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
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
                        view.getContext());
                elvTransactions.setAdapter(mTransactionsAdapter);
                elvTransactions.expandGroup(0);
                elvTransactions.setGroupIndicator(null);
            }
        });

        return view;
    }

    private void setupTransactions(List<Transaction> transactions) {
        mTransactionsGroup = new ArrayList<TransactionGroup>();
        mSpentToday = CurrencyUtils.newCurrency(0d);
        mSpentThisWeek = CurrencyUtils.newCurrency(0d);
        mLiquidAssets = CurrencyUtils.newCurrency(0d);
        
        TransactionGroup tg = null;
        int index = -1;

        for (Transaction t : transactions) {
            if (t.getType().equals(TransactionType.DEBIT)) {
                if (DateUtils.isToday(t.getTransactionDate())) {
                    mSpentToday = mSpentToday.add(BigDecimal.valueOf(t.getAmount()));
                }
                
                if (DateUtils.isSameWeek(t.getTransactionDate())) {
                    mSpentThisWeek = mSpentThisWeek.add(BigDecimal.valueOf(t.getAmount()));
                }

                // Set transactions for ListView
                tg = new TransactionGroup(t.getTransactionDate(), new ArrayList<Transaction>());
                index = mTransactionsGroup.indexOf(tg);
                
                if (index == -1) {
                    tg.getTransactions().add(t);
                    mTransactionsGroup.add(tg);
                } else {
                    mTransactionsGroup.get(index).getTransactions().add(t);
                }
            } else if (t.getType().equals(TransactionType.CREDIT)) {
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

    private void setupChart() {
        PieSlice slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.liquid_asset_background));
        slice.setValue(mLiquidAssets.floatValue());
        slice.setGoalValue(mLiquidAssets.floatValue());
        pgLiquidAssetDonutChart.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.light_red));
        slice.setGoalValue(mSpentToday.floatValue());
        pgLiquidAssetDonutChart.addSlice(slice);

        pgLiquidAssetDonutChart.setInnerCircleRatio(190);

        pgLiquidAssetDonutChart.setDuration(2000);
        pgLiquidAssetDonutChart.setInterpolator(new AccelerateDecelerateInterpolator());
        pgLiquidAssetDonutChart.animateToGoalValues();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == FragmentActivity.RESULT_OK && requestCode == ADD_TRANSACTION_REQUEST_CODE) {
            // TODO: refresh transaction list
            Toast.makeText(getActivity(), getString(R.string.parse_success_transaction_save),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
