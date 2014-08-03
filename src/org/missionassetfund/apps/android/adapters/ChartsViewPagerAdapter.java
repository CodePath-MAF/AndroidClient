package org.missionassetfund.apps.android.adapters;

import org.missionassetfund.apps.android.fragments.DailyTransactionsChartFragment;
import org.missionassetfund.apps.android.fragments.DailyTransactionsDetailChartFragment;
import org.missionassetfund.apps.android.models.Chart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ChartsViewPagerAdapter extends FragmentStatePagerAdapter {
    
    private Chart mChart;
    
    public ChartsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    public Chart getChart() {
        return mChart;
    }

    public void setChart(Chart chart) {
        this.mChart = chart;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        
        switch (position) {
            case 0:
                fragment = new DailyTransactionsChartFragment();
                ((DailyTransactionsChartFragment) fragment).setChart(mChart);
                break;
            case 1:
                fragment = new DailyTransactionsDetailChartFragment();
                // FIXME
//                ((DailyTransactionsDetailChartFragment) fragment).setTransactionGroup(mTransactionGroup);
                break;
            default:
                break;
        }
        
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    
}
