
package org.missionassetfund.apps.android.activities;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.fragments.GoalPaymentFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class GoalDetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_goal_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:
                // nothign yet
                break;
            case R.id.action_add_payments:
                FragmentManager fm = getSupportFragmentManager();
                GoalPaymentFragment newGoalPaymentFragment = GoalPaymentFragment.newInstance();
                newGoalPaymentFragment.show(fm, "fragment_new_goal");
                break;
            default:
                break;
        }

        return true;
    }

}
