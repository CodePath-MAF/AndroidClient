
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class DashboardFragment extends Fragment {
    public static final int ADD_TRANSACTION_REQUEST_CODE = 1;

    private RelativeLayout rlLiquidAsset;
    private TextView tvLiquidAsset;
    private ImageButton btnAddTransaction;

    private LinearLayout llTotalCashProgress;
    private LinearLayout llTotalCash;

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

        setupListeners();

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
        btnAddTransaction = (ImageButton) v.findViewById(R.id.btnAddTransaction);

        llTotalCash = (LinearLayout) v.findViewById(R.id.llTotalCash);
        llTotalCashProgress = (LinearLayout) v.findViewById(R.id.llTotalCashProgress);

        tvLiquidAsset.setText(CurrencyUtils.getCurrencyValueFormatted(CurrencyUtils.ZERO));
    }

    private void setupListeners() {
        rlLiquidAsset.setOnClickListener(liquidAssetClickListener);

        btnAddTransaction.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addTransactionIntent = new Intent(getActivity(),
                        AddTransactionActivity.class);
                startActivityForResult(addTransactionIntent, ADD_TRANSACTION_REQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
    }

    private void setupUserData() {
        // TODO(jose): Do calculation using Parse Cloud Code

        refreshTotalCash();
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

    private void refreshGoalList() {
        GoalsListFragment fragmentGoalList = (GoalsListFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.goalListFragment);
        fragmentGoalList.updateGoalList();
    }

    @Override
    public void onResume() {
        refreshTotalCash();
        refreshGoalList();
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshTotalCash();
        if (resultCode == FragmentActivity.RESULT_OK && requestCode == ADD_TRANSACTION_REQUEST_CODE) {
            Toast.makeText(getActivity(), getString(R.string.parse_success_transaction_save),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showTotalCashProgressBar() {
        llTotalCash.setVisibility(View.GONE);
        llTotalCashProgress.setVisibility(View.VISIBLE);
    }

    private void hideTotalCashProgressBar() {
        llTotalCash.setVisibility(View.VISIBLE);
        llTotalCashProgress.setVisibility(View.GONE);
    }
}
