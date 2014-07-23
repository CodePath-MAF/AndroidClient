
package org.missionassetfund.apps.android.activities;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.fragments.DailyTransactionsChartFragment.OnTransactionGroupClickedListener;
import org.missionassetfund.apps.android.fragments.LiquidAssetsFragment;
import org.missionassetfund.apps.android.models.TransactionGroup;

import android.os.Bundle;
import android.view.MenuItem;

public class LiquidAssetsActivity extends BaseFragmentActivity implements
        OnTransactionGroupClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquid_assets);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
