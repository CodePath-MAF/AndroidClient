
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;

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
import android.widget.LinearLayout;

public class DashboardFragment extends Fragment {
    LinearLayout llGoal;

    private LinearLayout llLiquidAsset;

    public interface SwitchMainFragmentListener {
        void SwitchToFragment(Class<? extends Fragment> klass);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        llGoal = (LinearLayout) view.findViewById(R.id.llGoal);
        setupListeners();

        llLiquidAsset = (LinearLayout) view.findViewById(R.id.llLiquidAsset);
        llLiquidAsset.setOnClickListener(liquidAssetClickListener);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard, menu);
    }

    private void setupListeners() {
        llGoal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SwitchMainFragmentListener fragmentChanger = (SwitchMainFragmentListener) getActivity();
                fragmentChanger.SwitchToFragment(GoalDetailFragment.class);
            }
        });

    }

    private OnClickListener liquidAssetClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), LiquidAssetsActivity.class);
            getActivity().startActivity(intent);
        }
    };
}
