
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;

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
    
    private LinearLayout llLiquidAsset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
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

    private OnClickListener liquidAssetClickListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), LiquidAssetsActivity.class);
            getActivity().startActivity(intent);
        }
    };
}
