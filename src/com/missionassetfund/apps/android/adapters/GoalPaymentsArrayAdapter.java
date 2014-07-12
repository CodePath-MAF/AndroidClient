
package com.missionassetfund.apps.android.adapters;

import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Transaction;

import com.missionassetfund.apps.android.utils.FormatterUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GoalPaymentsArrayAdapter extends ArrayAdapter<Transaction> {

    static class ViewHolder {
        TextView tvPastPaymentDate;
        TextView tvPastPaymentNumber;
        TextView tvPastPaymentAmount;
    }

    private final int layoutResource;
    private final int numTotalPayments;
    private final int numPaymentsDone;

    // private final Context context;

    public GoalPaymentsArrayAdapter(final Context context, int layoutResource,
            List<Transaction> goalPayments, int numTotalPayments) {
        super(context, layoutResource, goalPayments);
        this.numPaymentsDone = goalPayments.size();
        this.numTotalPayments = numTotalPayments;
        // this.context = context;
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction txn = this.getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = (View) inflator.inflate(layoutResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvPastPaymentDate = (TextView) convertView
                    .findViewById(R.id.tvPastPaymentDate);
            viewHolder.tvPastPaymentNumber = (TextView) convertView
                    .findViewById(R.id.tvPastPaymentNumber);
            viewHolder.tvPastPaymentAmount = (TextView) convertView
                    .findViewById(R.id.tvPastPaymentAmount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvPastPaymentAmount.setText(FormatterUtils.formatAmount(txn.getAmount()));
        viewHolder.tvPastPaymentNumber.setText("Payment " + (numPaymentsDone - position) + " of "
                + numTotalPayments);
        viewHolder.tvPastPaymentDate.setText(FormatterUtils.formatMonthDate(txn.getCreatedAt()));

        return convertView;
    }
}
