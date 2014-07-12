package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.ExpensesExpandableListAdapter;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.TransactionGroup;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

public class LiquidAssetsFragment extends Fragment {
    
    // FIXME temporary stuff
    private static final BigDecimal SPENT_AMOUNT = new BigDecimal(-123.45d);
    private static final BigDecimal REMAINING_AMOUNT = new BigDecimal(234.56d);
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private PieGraph pgLiquidAssetDonutChart;
    private TextView tvRemainingAmount;
    private TextView tvSpentAmount;
    private List<TransactionGroup> mTransactionsGroup;
    private ExpandableListView elvExpenses;
    private ExpensesExpandableListAdapter mExpensesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liquid_assets, container, false);
        
        pgLiquidAssetDonutChart = (PieGraph) view.findViewById(R.id.liquid_assets_donut_chart);
        tvRemainingAmount = (TextView) view.findViewById(R.id.tv_remaining_amount);
        tvSpentAmount = (TextView) view.findViewById(R.id.tv_spent_amount);
        elvExpenses = (ExpandableListView) view.findViewById(R.id.elvExpenses);
        
        setupChart();
        setupSummary();
        setupExpenses();
        
        mExpensesAdapter = new ExpensesExpandableListAdapter(mTransactionsGroup, view.getContext());
        elvExpenses.setAdapter(mExpensesAdapter);
        elvExpenses.expandGroup(0);
        elvExpenses.setGroupIndicator(null);
        
        return view;
    }

    private void setupExpenses() {
        List<Transaction> transactions = new ArrayList<>();
        Category category = new Category();
        
        Transaction transaction = new Transaction();
        transaction.setAmount(50d);
        // FIXME created at is a internal field
        transaction.setCreatedAt(this.parse("07/11/2014"));
        category.setName("Dinners & Drinks");
        transaction.setCategory(category);
        transactions.add(transaction);
        
        transaction = new Transaction();
        transaction.setAmount(60d);
        // FIXME created at is a internal field
        transaction.setCreatedAt(this.parse("07/09/2014"));
        category.setName("Breakfast");
        transaction.setCategory(category);
        transactions.add(transaction);

        transaction = new Transaction();
        transaction.setAmount(40d);
        // FIXME created at is a internal field
        transaction.setCreatedAt(this.parse("07/08/2014"));
        category.setName("Coffee");
        transaction.setCategory(category);
        transactions.add(transaction);

        transaction = new Transaction();
        transaction.setAmount(30d);
        // FIXME created at is a internal field
        transaction.setCreatedAt(this.parse("07/07/2014"));
        category.setName("Dinners & Drinks");
        transaction.setCategory(category);
        transactions.add(transaction);
        
        transaction = new Transaction();
        transaction.setAmount(30d);
        // FIXME created at is a internal field
        transaction.setCreatedAt(this.parse("07/01/2014"));
        category.setName("Dinners & Drinks");
        transaction.setCategory(category);
        transactions.add(transaction);
        
        transaction = new Transaction();
        transaction.setAmount(30d);
        // FIXME created at is a internal field
        transaction.setCreatedAt(this.parse("06/07/2014"));
        category.setName("Breakfast");
        transaction.setCategory(category);
        transactions.add(transaction);

        mTransactionsGroup = new ArrayList<TransactionGroup>();
        TransactionGroup tg = null;
        int index = -1;
        
        for (Transaction t : transactions) {
            tg = new TransactionGroup(t.getCreatedAt(), new ArrayList<Transaction>());
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

}
