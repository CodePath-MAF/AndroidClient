
package org.missionassetfund.apps.android.adapters;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.utils.FormatterUtils;
import org.missionassetfund.apps.android.utils.MAFDateUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class GoalAdapter extends ParseQueryAdapter<Goal> {

    public GoalAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Goal>() {
            @Override
            public ParseQuery<Goal> create() {
                ParseQuery<Goal> query = ParseQuery.getQuery(Goal.class);
                query.whereEqualTo("user", (User) User.getCurrentUser());
                query.addDescendingOrder("createdAt");
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

        // Populate Goal Item
        tvName.setText(goal.getName());
        // set human-friendly due dates
        int daysToDueDate = MAFDateUtils.getDaysTo(goal.getDueDate());
        if (daysToDueDate > 1) {
            if (daysToDueDate < 7) {
                tvDueDate.setText(getContext()
                        .getString(R.string.goal_due_date_days, daysToDueDate));
            } else {
                tvDueDate.setText(getContext().getString(R.string.goal_due_date,
                        FormatterUtils.formatMonthDate(goal.getDueDate())));
            }
        } else if (daysToDueDate == 1) {
            tvDueDate.setText(getContext().getString(R.string.goal_due_date_tomorrow));
        } else {
            tvDueDate.setText(getContext().getString(R.string.goal_due_date_today));
        }
        
        tvPaymentDue.setText(CurrencyUtils.getCurrencyValueFormatted(goal.getPaymentAmount()));

        return v;
    }
}
