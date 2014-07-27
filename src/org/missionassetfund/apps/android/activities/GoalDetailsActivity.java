
package org.missionassetfund.apps.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.GoalDetailsVPAdapter;
import org.missionassetfund.apps.android.fragments.LendingCircleProfilesFragment;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.GoalType;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.utils.FormatterUtils;
import org.missionassetfund.apps.android.utils.ParseUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.viewpagerindicator.CirclePageIndicator;

public class GoalDetailsActivity extends BaseFragmentActivity {

    private static final int ADD_PAYMENT_REQUEST_CODE = 100;

    private Goal goal;

    RelativeLayout rlLendingCircle;

    TextView tvPaymentDue;
    TextView tvDueDateHuman;
    TextView tvNumPayments;
    ProgressBar pbGoalPayment;

    TextView tvTotalTargetPayment;
    TextView tvSavedToDate;

    List<Transaction> goalPayments;

    GoalDetailsVPAdapter ldVPAdapter;
    ViewPager vpLendingCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);

        goalPayments = new ArrayList<Transaction>();
        ldVPAdapter = new GoalDetailsVPAdapter(getSupportFragmentManager(),
                getLendingCircleFriendsAdapter());

        rlLendingCircle = (RelativeLayout) findViewById(R.id.rlLendingCircle);
        tvPaymentDue = (TextView) findViewById(R.id.tvPaymentDue);
        tvDueDateHuman = (TextView) findViewById(R.id.tvDueDateHuman);
        tvNumPayments = (TextView) findViewById(R.id.tvNumPayments);
        pbGoalPayment = (ProgressBar) findViewById(R.id.pbGoalPayment);
        tvTotalTargetPayment = (TextView) findViewById(R.id.tvTotalTargetPayment);
        tvSavedToDate = (TextView) findViewById(R.id.tvSavedToDate);

        vpLendingCircle = (ViewPager) findViewById(R.id.vpLendingCircle);
        vpLendingCircle.setAdapter(ldVPAdapter);

        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(vpLendingCircle);

        String goalId = getIntent().getStringExtra(Goal.GOAL_KEY);

        // Goal was pinned when calling goal details activity.
        // Querying form local datastore.
        ParseQuery<Goal> query = ParseQuery.getQuery(Goal.class);
        query.fromLocalDatastore();

        query.getInBackground(goalId, new GetCallback<Goal>() {

            @Override
            public void done(Goal g, ParseException e) {
                if (e == null) {
                    goal = g;
                    // Setup Activity title base on the goal name
                    setTitle(g.getName());
                    populateViews();
                } else {
                    Toast.makeText(GoalDetailsActivity.this, R.string.parse_error_querying,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void populateViews() {
        // Once goal and payments are available let's setup views
        if (goal.getType() != null && goal.getType() == GoalType.LENDING_CIRCLE) {
            rlLendingCircle.setVisibility(View.VISIBLE);
        }

        tvTotalTargetPayment.setText(CurrencyUtils.getCurrencyValueFormatted(goal.getAmount()));

        tvDueDateHuman.setText(FormatterUtils.getGoalDueDateCustomFormat(
                GoalDetailsActivity.this, goal.getDueDate()));

        tvPaymentDue.setText(CurrencyUtils.getCurrencyValueFormatted(getPaymentsDue()));

        tvNumPayments.setText(getString(R.string.label_goal_payments_made, getPaymentsMade(),
                goal.getNumPayments()));

        pbGoalPayment.setProgress(getGoalProgress());

        tvSavedToDate.setText(getString(R.string.label_saved_to_date,
                CurrencyUtils.getCurrencyValueFormatted(goal.getCurrentTotal())));
    }

    private Double getPaymentsDue() {
        int idealNumPayments = goal.getIdealNumPaymentsTillToday();
        Double idealPaymentsTotal = (idealNumPayments + 1) * goal.getPaymentAmount();
        return Math.max((idealPaymentsTotal - goal.getCurrentTotal()), 0);
    }

    public void onMakePayment(View v) {
        Intent addPaymentIntent = new Intent(this, AddGoalPaymentActivity.class);
        addPaymentIntent.putExtra(Goal.GOAL_KEY, goal.getObjectId());
        startActivityForResult(addPaymentIntent, ADD_PAYMENT_REQUEST_CODE);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ADD_PAYMENT_REQUEST_CODE) {
            // Refresh Goal Detail view
            String objectId = data.getExtras().getString(Goal.GOAL_KEY);
            goal = ParseObject.createWithoutData(Goal.class, objectId);
            goal.fetchInBackground(new GetCallback<Goal>() {

                @Override
                public void done(Goal g, ParseException e) {
                    if (e == null) {
                        populateViews();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        goal.unpinInBackground(ParseUtils.DELETE_CALLBACK);
        super.onDestroy();
    }

    private int getGoalProgress() {
        return (int) ((goal.getCurrentTotal() * pbGoalPayment.getMax()) / goal.getAmount());
    }

    private int getPaymentsMade() {
        int paymentAmountInCents = (int) (goal.getPaymentAmount() * 100);
        int paymentsMade = (int) (goal.getCurrentTotal() * 100 / paymentAmountInCents);
        return paymentsMade;
    }

    private List<Fragment> getLendingCircleFriendsAdapter() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(LendingCircleProfilesFragment.newInstance(0));
        fList.add(LendingCircleProfilesFragment.newInstance(4));
        fList.add(LendingCircleProfilesFragment.newInstance(8));

        return fList;
    }

}
