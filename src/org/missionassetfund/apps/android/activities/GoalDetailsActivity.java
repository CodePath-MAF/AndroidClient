
package org.missionassetfund.apps.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.GoalDetailsVPAdapter;
import org.missionassetfund.apps.android.fragments.GoalPaymentFragment;
import org.missionassetfund.apps.android.fragments.GoalPaymentFragment.UpdatePaymentsListener;
import org.missionassetfund.apps.android.fragments.LendingCircleProfilesFragment;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.utils.FormatterUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.viewpagerindicator.CirclePageIndicator;

public class GoalDetailsActivity extends BaseFragmentActivity implements UpdatePaymentsListener {

    private Goal goal;

    RelativeLayout rlLendingCircle;

    TextView tvPaymentDue;
    // TextView tvDueDate;
    TextView tvDueDateHuman;
    TextView tvNumPayments;
    ProgressBar pbGoalPayment;

    // TextView tvTotalTargetPayment;
    // TextView tvTargetDate;
    // TextView tvTargetDateHuman;

    List<Transaction> goalPayments;
    // ListView lvPastPayments;
    // GoalPaymentsArrayAdapter paymentsAdapter;

    GoalDetailsVPAdapter ldVPAdapter;
    ViewPager vpLendingCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);

        rlLendingCircle = (RelativeLayout) findViewById(R.id.rlLendingCircle);

        tvPaymentDue = (TextView) findViewById(R.id.tvPaymentDue);
        // tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        tvDueDateHuman = (TextView) findViewById(R.id.tvDueDateHuman);
        tvNumPayments = (TextView) findViewById(R.id.tvNumPayments);
        pbGoalPayment = (ProgressBar) findViewById(R.id.pbGoalPayment);

        // tvTotalTargetPayment = (TextView)
        // findViewById(R.id.tvTotalTargetPayment);
        // tvTargetDate = (TextView) findViewById(R.id.tvTargetDate);
        // tvTargetDateHuman = (TextView) findViewById(R.id.tvTargetDateHuman);
        // lvPastPayments = (ListView) findViewById(R.id.lvPastPayments);
        goalPayments = new ArrayList<Transaction>();

        ldVPAdapter = new GoalDetailsVPAdapter(getSupportFragmentManager(),
                getLendingCircleFriendsAdapter());

        vpLendingCircle = (ViewPager) findViewById(R.id.vpLendingCircle);
        vpLendingCircle.setAdapter(ldVPAdapter);

        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(vpLendingCircle);

        // goal = (Goal) getIntent().getSerializableExtra(Goal.GOAL_KEY);
        String goalId = getIntent().getStringExtra(Goal.GOAL_KEY);

        // TODO(amit) consume goal being set from the intent
        // Goal will come from Dashboard. For now let's get one from parse
        ParseQuery<Goal> query = ParseQuery.getQuery(Goal.class);
        // query.whereEqualTo("user", (User) ParseUser.getCurrentUser());
        // query.addDescendingOrder("createdAt");

        query.getInBackground(goalId, new GetCallback<Goal>() {

            @Override
            public void done(Goal g, ParseException e) {
                if (e == null) {
                    goal = g;
                    setupPaymentsAdapter();
                    populateViews();
                } else {
                    Toast.makeText(GoalDetailsActivity.this, "Error getting goal",
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
                    // paymentsAdapter = new
                    // GoalPaymentsArrayAdapter(GoalDetailsActivity.this,
                    // R.layout.item_past_payment, goalPayments,
                    // goal.getNumPayments());
                    // lvPastPayments.setAdapter(paymentsAdapter);
                } else {
                    Log.e("error", "", e);
                }
            }
        });
    }

    private void populateViews() {
        // Once goal is available let's setup views
        // If goal is of type lending circle then change visibility to visible
        // rlLendingCircle.setVisibility(View.GONE);

        // tvTotalTargetPayment.setText(Double.toString(goal.getAmount()));
        // tvTargetDate.setText(FormatterUtils.formatMonthDate(goal.getGoalDate()));
        // tvTargetDateHuman.setText(FormatterUtils.getRelativeTimeHuman(goal.getGoalDate()));

        int idealNumPayments = goal.getIdealNumPaymentsTillToday();
        // Payment related fields will need all payment to be analyzed.
        Double idealPaymentsTotal = (idealNumPayments + 1) * goal.getPaymentAmount();
        Double paymentsDone = getPaymentsDone();
        int paymentsMade = (int) (paymentsDone / goal.getPaymentAmount());
        Double paymentsDue = idealPaymentsTotal - paymentsDone;

        tvPaymentDue.setText(FormatterUtils.formatAmount(paymentsDue));

        // tvDueDate.setText(FormatterUtils.formatMonthDate(goal.getDueDate()));
        tvDueDateHuman.setText(FormatterUtils.getGoalDueDateCustomFormat(
                GoalDetailsActivity.this, goal.getDueDate()));

        tvNumPayments.setText(getString(R.string.label_goal_payments_made, paymentsMade,
                goal.getNumPayments()));

        pbGoalPayment.setProgress(
                (int) ((paymentsDone * pbGoalPayment.getMax()) / goal.getAmount()));
    }

    public void onMakePayment(View v) {
        FragmentManager fm = getSupportFragmentManager();
        GoalPaymentFragment newGoalPaymentFragment = GoalPaymentFragment.newInstance(goal);
        newGoalPaymentFragment.show(fm, "fragment_new_goal");
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
            case R.id.action_add_payments:
                // FragmentManager fm = getSupportFragmentManager();
                // GoalPaymentFragment newGoalPaymentFragment =
                // GoalPaymentFragment.newInstance(goal);
                // newGoalPaymentFragment.show(fm, "fragment_new_goal");
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updatePayment(Transaction txn) {
        // paymentsAdapter.insert(txn, 0);
    }

    private List<Fragment> getLendingCircleFriendsAdapter() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(LendingCircleProfilesFragment.newInstance("Fragment 1"));
        fList.add(LendingCircleProfilesFragment.newInstance("Fragment 2"));
        fList.add(LendingCircleProfilesFragment.newInstance("Fragment 3"));

        return fList;
    }

}
