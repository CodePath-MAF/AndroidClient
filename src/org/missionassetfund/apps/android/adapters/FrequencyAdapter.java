
package org.missionassetfund.apps.android.adapters;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.GoalPaymentInterval;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class FrequencyAdapter extends ArrayAdapter<GoalPaymentInterval> implements SpinnerAdapter {

    public FrequencyAdapter(Context context, GoalPaymentInterval[] paymentIntervals) {
        super(context, R.layout.item_frequency, paymentIntervals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoalPaymentInterval paymentInterval = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_frequency,
                    parent, false);
        }
        TextView tvFrequency = (TextView) convertView.findViewById(R.id.tvFrequency);

        tvFrequency.setText(paymentInterval.toString());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        GoalPaymentInterval paymentInterval = getItem(position);

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.item_frequency_dropdown, parent, false);
        }

        TextView tvFrequency = (TextView) convertView.findViewById(R.id.tvFrequency);
        tvFrequency.setText(paymentInterval.toString());

        return convertView;
    }
}
