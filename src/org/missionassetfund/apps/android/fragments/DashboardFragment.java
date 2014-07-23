
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class DashboardFragment extends Fragment {
    private RelativeLayout rlLiquidAsset;
    private TextView tvLiquidAsset;
    private TextView tvSavedToday;
    private TextView tvSpentToday;

    private LinearLayout llTotalCashProgress;
    private LinearLayout llTotalCash;
    private LinearLayout llSavedTodayProgress;
    private LinearLayout llSavedToday;
    private LinearLayout llSpentTodayProgress;
    private LinearLayout llSpentToday;

    public interface SwitchMainFragmentListener {
        void SwitchToFragment(Class<? extends Fragment> klass);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.dashboard_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        setupViews(view);

        rlLiquidAsset.setOnClickListener(liquidAssetClickListener);

        setupUserData();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard, menu);
    }

    private void setupViews(View v) {
        rlLiquidAsset = (RelativeLayout) v.findViewById(R.id.rlLiquidAsset);
        tvLiquidAsset = (TextView) v.findViewById(R.id.tvLiquidAsset);
        tvSavedToday = (TextView) v.findViewById(R.id.tvSavedToday);
        tvSpentToday = (TextView) v.findViewById(R.id.tvSpentToday);

        llTotalCash = (LinearLayout) v.findViewById(R.id.llTotalCash);
        llSavedToday = (LinearLayout) v.findViewById(R.id.llSavedToday);
        llSpentToday = (LinearLayout) v.findViewById(R.id.llSpentToday);
        llTotalCashProgress = (LinearLayout) v.findViewById(R.id.llTotalCashProgress);
        llSavedTodayProgress = (LinearLayout) v.findViewById(R.id.llSavedTodayProgress);
        llSpentTodayProgress = (LinearLayout) v.findViewById(R.id.llSpentTodayProgress);

        tvLiquidAsset.setText(CurrencyUtils.getCurrencyValueFormatted(CurrencyUtils.ZERO));
        tvSavedToday.setText(CurrencyUtils.getCurrencyValueFormatted(CurrencyUtils.ZERO));
        tvSpentToday.setText(CurrencyUtils.getCurrencyValueFormatted(CurrencyUtils.ZERO));
    }

    private void setupUserData() {
        // TODO(jose): Do calculation using Parse Cloud Code

        refreshTotalCash();
        refreshSpentToday();
        refreshSavedToday();
    }

    private OnClickListener liquidAssetClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), LiquidAssetsActivity.class);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    };

    private void refreshTotalCash() {
        showTotalCashProgressBar();

        // Calculate Liquid Asset balance
        ParseQuery<Transaction> query = ParseQuery.getQuery(Transaction.class);
        query.whereEqualTo(Transaction.USER_KEY, User.getCurrentUser());
        query.setLimit(500);
        query.findInBackground(new FindCallback<Transaction>() {

            @Override
            public void done(List<Transaction> results, ParseException e) {
                if (e != null) {
                    hideTotalCashProgressBar();
                    Toast.makeText(getActivity(), getString(R.string.parse_error_querying),
                            Toast.LENGTH_LONG).show();
                    Log.d("DEBUG", e.getMessage());
                } else {
                    BigDecimal totalCash = CurrencyUtils.newCurrency(0d);
                    for (Transaction t : results) {
                        if (t.isDebit()) {
                            totalCash = totalCash.add(CurrencyUtils.newCurrency(t.getAmount()));
                        } else {
                            totalCash = totalCash.subtract(CurrencyUtils.newCurrency(t.getAmount()));
                        }
                    }

                    // Set values into the view
                    tvLiquidAsset.setText(CurrencyUtils.getCurrencyValueFormatted(totalCash));

                    hideTotalCashProgressBar();
                }
            }
        });
    }

    private void refreshSpentToday() {
        showSpentTodayProgressBar();

        // Calculate Liquid Asset balance
        ParseQuery<Transaction> query = ParseQuery.getQuery(Transaction.class);
        query.whereEqualTo(Transaction.USER_KEY, User.getCurrentUser());
        query.setLimit(500);
        query.findInBackground(new FindCallback<Transaction>() {

            @Override
            public void done(List<Transaction> results, ParseException e) {
                if (e != null) {
                    hideSpentTodayProgressBar();
                    Toast.makeText(getActivity(), getString(R.string.parse_error_querying),
                            Toast.LENGTH_LONG).show();
                    Log.d("DEBUG", e.getMessage());
                } else {
                    BigDecimal spentToday = CurrencyUtils.newCurrency(0d);
                    for (Transaction t : results) {
                        // Check Spents Today.
                        if (DateUtils.isToday(t.getTransactionDate().getTime())
                                && t.isCredit()) {
                            spentToday = spentToday.subtract(CurrencyUtils.newCurrency(t
                                    .getAmount()));
                        }
                    }

                    // Customize spent today text view
                    if (spentToday.compareTo(CurrencyUtils.newCurrency(0d)) == -1) {
                        tvSpentToday.setTextAppearance(getActivity(),
                                R.style.DashboardUI_SpentToday);
                    }

                    // Set values into the view
                    tvSpentToday.setText(CurrencyUtils.getCurrencyValueFormatted(spentToday));

                    hideSpentTodayProgressBar();
                }
            }
        });
    }

    private void refreshSavedToday() {
        showMonthlyGoalProgressBar();

        // Calculate Liquid Asset balance
        ParseQuery<Transaction> query = ParseQuery.getQuery(Transaction.class);
        query.whereEqualTo(Transaction.USER_KEY, User.getCurrentUser());
        query.whereNotEqualTo(Transaction.GOAL_KEY, null);
        query.setLimit(500);
        query.findInBackground(new FindCallback<Transaction>() {

            @Override
            public void done(List<Transaction> results, ParseException e) {
                if (e != null) {
                    hideMonthlyGoalProgressBar();
                    Toast.makeText(getActivity(), getString(R.string.parse_error_querying),
                            Toast.LENGTH_LONG).show();
                    Log.d("DEBUG", e.getMessage());
                } else {
                    BigDecimal savedToday = CurrencyUtils.newCurrency(0d);
                    for (Transaction t : results) {
                        // Check Goal Today.
                        if (DateUtils.isToday(t.getTransactionDate().getTime())
                                && t.isCredit()) {
                            savedToday = savedToday.add(CurrencyUtils.newCurrency(t
                                    .getAmount()));
                        }
                    }

                    // Set values into the view
                    tvSavedToday.setText(CurrencyUtils.getCurrencyValueFormatted(savedToday));

                    hideMonthlyGoalProgressBar();
                }
            }
        });
    }

    private void refreshGoalList() {
        GoalsListFragment fragmentGoalList = (GoalsListFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.goalListFragment);
        fragmentGoalList.updateGoalList();
    }

    @Override
    public void onResume() {
        refreshTotalCash();
        refreshSpentToday();
        refreshGoalList();
        super.onResume();
    }

    private void showTotalCashProgressBar() {
        llTotalCash.setVisibility(View.GONE);
        llTotalCashProgress.setVisibility(View.VISIBLE);
    }

    private void hideTotalCashProgressBar() {
        llTotalCash.setVisibility(View.VISIBLE);
        llTotalCashProgress.setVisibility(View.GONE);
    }

    private void showMonthlyGoalProgressBar() {
        llSavedToday.setVisibility(View.GONE);
        llSavedTodayProgress.setVisibility(View.VISIBLE);
    }

    private void hideMonthlyGoalProgressBar() {
        llSavedToday.setVisibility(View.VISIBLE);
        llSavedTodayProgress.setVisibility(View.GONE);
    }

    private void showSpentTodayProgressBar() {
        llSpentToday.setVisibility(View.GONE);
        llSpentTodayProgress.setVisibility(View.VISIBLE);
    }

    private void hideSpentTodayProgressBar() {
        llSpentToday.setVisibility(View.VISIBLE);
        llSpentTodayProgress.setVisibility(View.GONE);
    }
}
