package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

public class LiquidAssetsFragment extends Fragment {
    
    private PieGraph pgLiquidAssetDonutChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liquid_assets, container, false);
        
        pgLiquidAssetDonutChart = (PieGraph) view.findViewById(R.id.liquid_assets_donut_chart);
        setupChart();
        
        return view;
    }

    private void setupChart() {
        // TODO Auto-generated method stub
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFFFFF"));
        slice.setValue(5);
        pgLiquidAssetDonutChart.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#00688B"));
        slice.setValue(5);
        pgLiquidAssetDonutChart.addSlice(slice);
        
        pgLiquidAssetDonutChart.setInnerCircleRatio(180);
    }
    

}
