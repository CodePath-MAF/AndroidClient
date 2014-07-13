
package org.missionassetfund.apps.android.adapters;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.FormatterUtils;

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
        // TODO(jose): Use goal due date buitl by amit. ATM just showing the Goal Date.
        tvDueDate.setText(FormatterUtils.formatMonthDate(goal.getGoalDate()));
        // TODO(jose): use CurrencyUtils.getCurrencyValueFormatted from felipe's PR
        tvPaymentDue.setText(FormatterUtils.formatAmount(goal.getPaymentAmount()));

        return v;
    }
}
