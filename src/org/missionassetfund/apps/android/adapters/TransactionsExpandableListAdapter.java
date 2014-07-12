
package org.missionassetfund.apps.android.adapters;

import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.TransactionGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class TransactionsExpandableListAdapter extends BaseExpandableListAdapter {

    private List<TransactionGroup> mTransactionsGroup;
    private LayoutInflater mInflater;

    public TransactionsExpandableListAdapter(List<TransactionGroup> mTransactionsGroup,
            Context context) {
        this.mTransactionsGroup = mTransactionsGroup;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return mTransactionsGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mTransactionsGroup.get(groupPosition).getTransactions().size();
    }

    @Override
    public TransactionGroup getGroup(int groupPosition) {
        return mTransactionsGroup.get(groupPosition);
    }

    @Override
    public Transaction getChild(int groupPosition, int childPosition) {
        return mTransactionsGroup.get(groupPosition).getTransactions().get(childPosition);
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        final CharSequence relativeDateString = this.getGroup(groupPosition).getRelativeDate();
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.transactions_list_group, null);
        }
        
        TextView tvExpenseDateGroup = (TextView) convertView.findViewById(R.id.tvTransactionDateGroup);
        tvExpenseDateGroup.setText(relativeDateString);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        Transaction transaction = this.getChild(groupPosition, childPosition);
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.transactions_list_detail, null);
        }

        TextView tvTransactionCategory = (TextView) convertView.findViewById(R.id.tvTransactionCategory);
        TextView tvTransactionAmount = (TextView) convertView.findViewById(R.id.tvTransactionAmount);

        tvTransactionCategory.setText(transaction.getCategory().getName());
        tvTransactionAmount.setText(transaction.getAmountFormatted());
        
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
