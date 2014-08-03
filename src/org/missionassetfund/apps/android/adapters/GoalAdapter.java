
package org.missionassetfund.apps.android.adapters;

import java.text.NumberFormat;
import java.util.Locale;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.utils.FormatterUtils;
import org.missionassetfund.apps.android.utils.MAFDateUtils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lylc.widget.circularprogressbar.example.CircularProgressBar;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class GoalAdapter extends ParseQueryAdapter<Goal> {

    public GoalAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Goal>() {
            @Override
            public ParseQuery<Goal> create() {
                ParseQuery<Goal> query = ParseQuery.getQuery(Goal.class);
                query.whereEqualTo(Goal.USER_KEY, (User) User.getCurrentUser());
                query.addAscendingOrder(Goal.GOAL_START_DATE_KEY);
                return query;
            }
        });
    }

    @Override
    public View getItemView(Goal goal, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_goal, null);
        }

        super.getItemView(goal, v, parent);

        // Look up view elements
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        TextView tvDueDate = (TextView) v.findViewById(R.id.tvDueDate);
        TextView tvPaymentDue = (TextView) v.findViewById(R.id.tvPaymentDue);
        TextView tvPctComplete = (TextView) v.findViewById(R.id.tvPctComplete);
        CircularProgressBar cpbGoalProgress = (CircularProgressBar) v
                .findViewById(R.id.cpbGoalProgress);

        // reset view style
        tvDueDate.setTextAppearance(getContext(), R.style.DashboardUI_GoalItem_DueDate);
        tvDueDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_time_gray, 0, 0, 0);

        // Populate Goal Item
        tvName.setText(goal.getName());
        // set human-friendly due dates
        int daysToDueDate = MAFDateUtils.getDaysTo(goal.getDueDate());
        if (daysToDueDate > 1) {
            if (daysToDueDate < 7) {
                tvDueDate.setText(getContext()
                        .getString(R.string.dashboard_goal_item_due_date_days, daysToDueDate)
                        .toUpperCase(Locale.US));
                setDueDateSoonStyle(tvDueDate);
            } else {
                tvDueDate.setText(getContext().getString(R.string.dashboard_goal_item_due_date,
                        FormatterUtils.formatMonthDate(goal.getDueDate())).toUpperCase(Locale.US));
            }
        } else if (daysToDueDate == 1) {
            tvDueDate
                    .setText(getContext().getString(R.string.dashboard_goal_item_due_date_tomorrow)
                            .toUpperCase(Locale.US));
            setDueDateSoonStyle(tvDueDate);
        } else {
            tvDueDate.setText(getContext().getString(R.string.dashboard_goal_item_due_date_today)
                    .toUpperCase(Locale.US));
            setDueDateSoonStyle(tvDueDate);
        }

        tvPaymentDue.setText(CurrencyUtils.getCurrencyValueFormatted(goal.getPaymentAmount()));

        float goalPctComplete = 0f;
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(1);
        String percentageComplete = "";
        if (goal.getNumPaymentsMade() != null) {
            goalPctComplete = (float) goal.getNumPaymentsMade() / goal.getNumPayments();
            percentageComplete = percentFormat.format(goalPctComplete);
        } else {
            percentageComplete = percentFormat.format(0f);
        }

        Log.d("DEBUG", percentageComplete);

        // circular progress bar with simple animation
        cpbGoalProgress.animateProgressTo(0, (int) (goalPctComplete * 100),
                new CircularProgressBar.ProgressAnimationListener() {

                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationProgress(int progress) {
                    }

                    @Override
                    public void onAnimationFinish() {
                    }
                });

        tvPctComplete.setText(percentageComplete);

        return v;
    }

    private void setDueDateSoonStyle(TextView tvDueDate) {
        tvDueDate.setTextAppearance(getContext(), R.style.DashboardUI_GoalItem_DueDateSoon);
        tvDueDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_time_red, 0, 0, 0);
    }
}
