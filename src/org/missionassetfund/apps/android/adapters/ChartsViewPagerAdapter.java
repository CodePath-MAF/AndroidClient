package org.missionassetfund.apps.android.adapters;

import java.util.List;

import org.missionassetfund.apps.android.fragments.DailyTransactionsChartFragment;
import org.missionassetfund.apps.android.fragments.DailyTransactionsDetailChartFragment;
import org.missionassetfund.apps.android.models.TransactionGroup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ChartsViewPagerAdapter extends FragmentStatePagerAdapter {
    
    private List<TransactionGroup> mTransactionGroups;
    private TransactionGroup mTransactionGroup;

    public ChartsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setTransactionGroups(List<TransactionGroup> transactionGroups) {
        this.mTransactionGroups = transactionGroups;
    }
    
    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.mTransactionGroup = transactionGroup;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        
        switch (position) {
            case 0:
                fragment = new DailyTransactionsChartFragment();
                ((DailyTransactionsChartFragment) fragment).setTransactionGroups(mTransactionGroups);
                break;
            case 1:
                fragment = new DailyTransactionsDetailChartFragment();
                ((DailyTransactionsDetailChartFragment) fragment).setTransactionGroup(mTransactionGroup);
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
