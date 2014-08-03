
package org.missionassetfund.apps.android.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.utils.MAFDateUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TransactionsExpandableListAdapter extends BaseExpandableListAdapter {

    private Map<String, List<Transaction>> mTransactionsByDate;
    private String[] mKeys;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private LayoutInflater mInflater;

    public TransactionsExpandableListAdapter(TreeMap<String, List<Transaction>> transactionsByDate,
            Context context) {
        mTransactionsByDate = transactionsByDate.descendingMap();
        mKeys = transactionsByDate.descendingKeySet().toArray(new String[transactionsByDate.size()]);
        mInflater = LayoutInflater.from(context);
    }

    public Date parse(String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }
    @Override
    public int getGroupCount() {
        return mKeys.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mTransactionsByDate.get(mKeys[groupPosition]).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        String date = mKeys[groupPosition];
        return MAFDateUtils.getRelativeDate(this.parse(date));
    }

    @Override
    public Transaction getChild(int groupPosition, int childPosition) {
        return mTransactionsByDate.get(mKeys[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        final CharSequence relativeDateString = this.getGroup(groupPosition);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.transactions_list_group, null);
        }

        TextView tvExpenseDateGroup = (TextView) convertView
                .findViewById(R.id.tvTransactionDateGroup);
        tvExpenseDateGroup.setText(relativeDateString.toString().toUpperCase(Locale.US));
        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        Transaction transaction = this.getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.transactions_list_detail, null);
        }

        TextView tvTransactionCategory = (TextView) convertView
                .findViewById(R.id.tvTransactionCategory);
        TextView tvTransactionAmount = (TextView) convertView
                .findViewById(R.id.tvTransactionAmount);
        TextView tvTransactionName = (TextView) convertView
                .findViewById(R.id.tvTransactionName);

        tvTransactionCategory.setText(transaction.getCategory() == null ? transaction
                .getName() : transaction.getCategory().getName().toUpperCase(Locale.US));
        tvTransactionAmount.setText(transaction.getAmountFormatted());
        tvTransactionName.setText(transaction.getName());

        if (transaction.getCategory() != null) {
            RelativeLayout rlTransactionsListDetail = (RelativeLayout) convertView
                    .findViewById(R.id.rlTransactionsListDetail);
            LayerDrawable layers = (LayerDrawable) rlTransactionsListDetail.getBackground();
            GradientDrawable shape = (GradientDrawable) layers.getDrawable(0);
            shape.setColor(Color.parseColor(transaction.getCategory().getColor()));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
