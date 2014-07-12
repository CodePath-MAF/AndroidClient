
package org.missionassetfund.apps.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.GoalPaymentsArrayAdapter;
import org.missionassetfund.apps.android.fragments.GoalPaymentFragment;
import org.missionassetfund.apps.android.fragments.GoalPaymentFragment.UpdatePaymentsListener;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.FormatterUtils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class GoalDetailsActivity extends FragmentActivity implements UpdatePaymentsListener {

    private Goal goal;

    TextView tvPaymentDue;
    TextView tvDueDate;
    TextView tvDueDateHuman;
    TextView tvTotalTargetPayment;
    TextView tvTargetDate;
    TextView tvTargetDateHuman;

    ListView lvPastPayments;
    List<Transaction> goalPayments;
    GoalPaymentsArrayAdapter paymentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);

        tvPaymentDue = (TextView) findViewById(R.id.tvPaymentDue);
        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        tvDueDateHuman = (TextView) findViewById(R.id.tvDueDateHuman);
        tvTotalTargetPayment = (TextView) findViewById(R.id.tvTotalTargetPayment);
        tvTargetDate = (TextView) findViewById(R.id.tvTargetDate);
        tvTargetDateHuman = (TextView) findViewById(R.id.tvTargetDateHuman);
        lvPastPayments = (ListView) findViewById(R.id.lvPastPayments);
        goalPayments = new ArrayList<Transaction>();

        // Goal will come from Dashboard. For now let's get one from parse
        ParseQuery<Goal> query = ParseQuery.getQuery(Goal.class);
        query.whereEqualTo("user", (User) ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> goals, ParseException e) {
                if (e == null) {
                    for (Goal g : goals) {
                        Log.d("debug", g.getName());
                        Toast.makeText(GoalDetailsActivity.this, g.getName(),
                                Toast.LENGTH_SHORT).show();
                        goal = g;
                        setupPaymentsAdapter();
                        break;
                    }
                    populateViews();
                } else {
                    Toast.makeText(GoalDetailsActivity.this, "Error getting goals",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void setupPaymentsAdapter() {
        ParseQuery<Transaction> query = ParseQuery.getQuery(Transaction.class);
        query.whereEqualTo("goal", goal);
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Transaction>() {

            @Override
            public void done(List<Transaction> txns, ParseException e) {
                if (e == null) {
                    goalPayments = txns;
                    paymentsAdapter = new GoalPaymentsArrayAdapter(GoalDetailsActivity.this,
                            R.layout.item_past_payment, goalPayments, goal.getNumPayments());
                    lvPastPayments.setAdapter(paymentsAdapter);
                } else {
                    Log.e("error", "", e);
                }
            }
        });

    }

    private void populateViews() {
        // Once goal is available let's setup views
        tvTotalTargetPayment.setText(Double.toString(goal.getAmount()));
        tvTargetDate.setText(FormatterUtils.formatMonthDate(goal.getGoalDate()));
        tvTargetDateHuman.setText(FormatterUtils.getRelativeTimeHuman(goal.getGoalDate()));

        int idealNumPayments = goal.getIdealNumPaymentsTillToday();
        // Payment related fields will need all payment to be analyzed.
        Double idealPaymentsTotal = idealNumPayments * goal.getPaymentAmount();
        Double paymentsDone = getPaymentsDone();
        Double paymentsDue = idealPaymentsTotal - paymentsDone;
        tvPaymentDue.setText(FormatterUtils.formatAmount(paymentsDue));

        tvDueDate.setText(FormatterUtils.formatMonthDate(goal.getDueDate()));
        tvDueDateHuman.setText(FormatterUtils.getRelativeTimeHuman(goal.getDueDate()));
    }

    private Double getPaymentsDone() {
        Double paymentsDone = 0.0;
        for (Transaction txn : goalPayments) {
            paymentsDone += txn.getAmount();
        }
        return paymentsDone;
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

    @Override
    public void updatePayment(Transaction txn) {
        paymentsAdapter.insert(txn, 0);
    }

}
