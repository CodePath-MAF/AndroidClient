
package org.missionassetfund.apps.android.activities;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.fragments.DailyTransactionsChartFragment.OnTransactionGroupClickedListener;
import org.missionassetfund.apps.android.fragments.LiquidAssetsFragment;
import org.missionassetfund.apps.android.models.TransactionGroup;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LiquidAssetsActivity extends FragmentActivity implements OnTransactionGroupClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquid_assets);
    }

    @Override
    public void onBarClicked(TransactionGroup transactionGroup) {
        LiquidAssetsFragment liquidAssetsFragment = (LiquidAssetsFragment)
                getSupportFragmentManager().findFragmentById(R.id.liquid_assets_fragment);

        if (liquidAssetsFragment != null) {
            liquidAssetsFragment.goToNextChart(transactionGroup);
        }
    }
}
