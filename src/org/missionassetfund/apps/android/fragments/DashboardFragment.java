
package org.missionassetfund.apps.android.fragments;

import java.text.NumberFormat;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;
import org.missionassetfund.apps.android.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DashboardFragment extends Fragment {
    private RelativeLayout rlLiquidAsset;
    private TextView tvLiquidAsset;
    private TextView tvMonthlyGoals;
    private TextView tvSpentToday;

    private User currentUser;

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

        currentUser = (User) User.getCurrentUser();
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
        // TODO(jose): investigate and potentially set up Parse Cloud Code to
        // create custom functions that would do aggregate queries.
        tvLiquidAsset.setText(getCurrencyValueFormatted(currentUser.getLiquidAssets()));
        tvMonthlyGoals.setText(getCurrencyValueFormatted(60.5d));
        tvSpentToday.setText(getCurrencyValueFormatted(-123.45d));
    }

    private OnClickListener liquidAssetClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), LiquidAssetsActivity.class);
            getActivity().startActivity(intent);
        }
    };

    private String getCurrencyValueFormatted(Double value) {
        NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
        return baseFormat.format(value);
    }

}
