
package org.missionassetfund.apps.android.activities;

import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.fragments.GoalPaymentFragment;
import org.missionassetfund.apps.android.models.Goal;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class GoalDetailsActivity extends FragmentActivity {

    private Goal goal;

    TextView tvTotalPayment;
    TextView tvDueDate;
    TextView tvDueDateHuman;
    TextView tvTotalTargetPayment;
    TextView tvTargetDate;
    TextView tvTargetDateHuman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);
        // Goal will come from Dashboard. For now let's get one from parse
        ParseQuery<Goal> query = ParseQuery.getQuery(Goal.class);
        // query.whereEqualTo("user", (User) ParseUser.getCurrentUser());
        query.whereEqualTo("name", "Test Goal Details");

        query.findInBackground(new FindCallback<Goal>() {

            @Override
            public void done(List<Goal> goals, ParseException e) {
                if (e == null) {
                    for (Goal g : goals) {
                        Log.d("debug", g.getName());
                        Toast.makeText(GoalDetailsActivity.this, g.getName(),
                                Toast.LENGTH_SHORT).show();
                        goal = g;
                        continue;
                    }
                } else {
                    Toast.makeText(GoalDetailsActivity.this, "Error getting goals",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
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
                GoalPaymentFragment newGoalPaymentFragment = GoalPaymentFragment.newInstance(goal);
                newGoalPaymentFragment.show(fm, "fragment_new_goal");
                break;
            default:
                break;
        }

        return true;
    }

}
