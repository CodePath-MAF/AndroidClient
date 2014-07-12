
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.ExpensesExpandableListAdapter;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.TransactionGroup;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;

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
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.parse.FindCallback;
import com.parse.ParseQuery;

public class LiquidAssetsFragment extends Fragment {
    
    // FIXME temporary stuff
    private static final BigDecimal SPENT_AMOUNT = new BigDecimal(-123.45d);
    private static final BigDecimal REMAINING_AMOUNT = new BigDecimal(234.56d);
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    public static final int ADD_TRANSACTION_REQUEST_CODE = 1;

    private PieGraph pgLiquidAssetDonutChart;
    private TextView tvRemainingAmount;
    private TextView tvSpentAmount;
    private List<TransactionGroup> mTransactionsGroup;
    private ExpandableListView elvExpenses;
    private ExpensesExpandableListAdapter mExpensesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_liquid_assets, container, false);

        pgLiquidAssetDonutChart = (PieGraph) view.findViewById(R.id.liquid_assets_donut_chart);
        tvRemainingAmount = (TextView) view.findViewById(R.id.tv_remaining_amount);
        tvSpentAmount = (TextView) view.findViewById(R.id.tv_spent_amount);
        elvExpenses = (ExpandableListView) view.findViewById(R.id.elvExpenses);
        
        setupChart();
        setupSummary();
        
        ParseQuery<Category> query = ParseQuery.getQuery("Category");
        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> categories, com.parse.ParseException exception) {
                if (exception != null) {
                    Log.d("LiquidAssetsFragment", "error on querying categories", exception);
                    return;
                }
                
                setupExpenses(categories);
                
                mExpensesAdapter = new ExpensesExpandableListAdapter(mTransactionsGroup, view.getContext());
                elvExpenses.setAdapter(mExpensesAdapter);
                elvExpenses.expandGroup(0);
                elvExpenses.setGroupIndicator(null);
            }
        });
        
        return view;
    }

    private void setupExpenses(List<Category> categories) {
        List<Transaction> transactions = new ArrayList<>();
        
        Transaction transaction = new Transaction();
        transaction.setAmount(50d);
        transaction.setTransactionDate(this.parse("07/12/2014"));
        Collections.shuffle(categories);
        transaction.setCategory(categories.get(0));
        transactions.add(transaction);
        
        transaction = new Transaction();
        transaction.setAmount(60d);
        transaction.setTransactionDate(this.parse("07/11/2014"));
        Collections.shuffle(categories);
        transaction.setCategory(categories.get(0));
        transactions.add(transaction);

        transaction = new Transaction();
        transaction.setAmount(40d);
        transaction.setTransactionDate(this.parse("07/07/2014"));
        Collections.shuffle(categories);
        transaction.setCategory(categories.get(0));
        transactions.add(transaction);

        transaction = new Transaction();
        transaction.setAmount(30d);
        transaction.setTransactionDate(this.parse("07/07/2014"));
        Collections.shuffle(categories);
        transaction.setCategory(categories.get(0));
        transactions.add(transaction);
        
        transaction = new Transaction();
        transaction.setAmount(30d);
        transaction.setTransactionDate(this.parse("07/01/2014"));
        Collections.shuffle(categories);
        transaction.setCategory(categories.get(0));
        transactions.add(transaction);
        
        transaction = new Transaction();
        transaction.setAmount(30d);
        transaction.setTransactionDate(this.parse("06/07/2014"));
        Collections.shuffle(categories);
        transaction.setCategory(categories.get(0));
        transactions.add(transaction);

        mTransactionsGroup = new ArrayList<TransactionGroup>();
        TransactionGroup tg = null;
        int index = -1;
        
        for (Transaction t : transactions) {
            tg = new TransactionGroup(t.getTransactionDate(), new ArrayList<Transaction>());
            index = mTransactionsGroup.indexOf(tg);
            
            if (index == -1) {
                tg.getTransactions().add(t);
                mTransactionsGroup.add(tg);
            } else {
                mTransactionsGroup.get(index).getTransactions().add(t);
            }
         }
        
    }
    
    private Date parse(String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return new Date();
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
        tvRemainingAmount.setText(CurrencyUtils.getCurrencyValueFormatted(REMAINING_AMOUNT));
        tvSpentAmount.setText(CurrencyUtils.getCurrencyValueFormatted(SPENT_AMOUNT));
    }

    private void setupChart() {
        PieSlice slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.white));
        slice.setValue(REMAINING_AMOUNT.floatValue());
        pgLiquidAssetDonutChart.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.navy_blue));
        slice.setValue(Math.abs(SPENT_AMOUNT.floatValue()));
        pgLiquidAssetDonutChart.addSlice(slice);

        pgLiquidAssetDonutChart.setInnerCircleRatio(180);
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
