
package org.missionassetfund.apps.android.fragments;

import java.text.NumberFormat;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.Transaction.TransactionType;
import org.missionassetfund.apps.android.models.User;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class DashboardFragment extends Fragment {
    private RelativeLayout rlLiquidAsset;
    private TextView tvLiquidAsset;
    private TextView tvMonthlyGoals;
    private TextView tvSpentToday;

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
        tvMonthlyGoals = (TextView) v.findViewById(R.id.tvMonthlyGoals);
        tvSpentToday = (TextView) v.findViewById(R.id.tvSpentToday);
    }

    private void setupUserData() {
        // TODO(jose): Do calculation using Parse Cloud Code

        // Calculate Liquid Asset balance
        ParseQuery<Transaction> query = ParseQuery.getQuery(Transaction.class);
        query.whereEqualTo("user", User.getCurrentUser());
        query.setLimit(500);
        query.findInBackground(new FindCallback<Transaction>() {

            @Override
            public void done(List<Transaction> results, ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), getString(R.string.parse_error_querying),
                            Toast.LENGTH_LONG).show();
                    tvLiquidAsset.setText(getCurrencyValueFormatted(0d));
                    tvMonthlyGoals.setText(getCurrencyValueFormatted(0d));
                    tvSpentToday.setText(getCurrencyValueFormatted(0d));
                    Log.d("DEBUG", e.getMessage());
                } else {
                    Double cla = 0d;
                    Double spentToday = 0d;
                    for (Transaction t : results) {
                        if (t.getType().equals(TransactionType.DEBIT)) {
                            cla += t.getAmount();
                        } else {
                            cla -= t.getAmount();
                            // Check Spents Today.
                            if (DateUtils.isToday(t.getTransactionDate().getTime())) {
                                spentToday -= t.getAmount();
                            }
                        }
                    }

                    // Set values into the view
                    tvLiquidAsset.setText(getCurrencyValueFormatted(cla));
                    tvSpentToday.setText(getCurrencyValueFormatted(spentToday));
                }
            }
        });

        // TODO(jose): Calculate Monthly Goals
    }

    private OnClickListener liquidAssetClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), LiquidAssetsActivity.class);
            // TODO(jose): start the activity for result and refresh the liquid
            // asset section once it returns
            getActivity().startActivity(intent);
        }
    };

    private String getCurrencyValueFormatted(Double value) {
        NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
        return baseFormat.format(value);
    }

}
