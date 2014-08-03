
package org.missionassetfund.apps.android.activities;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.fragments.DailyTransactionsChartFragment.OnTransactionsBarClickedListener;
import org.missionassetfund.apps.android.fragments.LiquidAssetsFragment;

import android.os.Bundle;
import android.view.MenuItem;

public class LiquidAssetsActivity extends BaseFragmentActivity implements
        OnTransactionsBarClickedListener {

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
    public void onBarClicked(int barIndex) {
        if (barIndex == -1) {
            return;
        }
        
        LiquidAssetsFragment liquidAssetsFragment = (LiquidAssetsFragment)
                getSupportFragmentManager().findFragmentById(R.id.liquid_assets_fragment);

        if (liquidAssetsFragment != null) {
            liquidAssetsFragment.goToNextChart(barIndex);
        }
    }
}
