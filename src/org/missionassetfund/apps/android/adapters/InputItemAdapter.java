
package org.missionassetfund.apps.android.adapters;

import java.util.ArrayList;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Input;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InputItemAdapter extends ArrayAdapter<Input> {

    public InputItemAdapter(Context context, ArrayList<Input> inputs) {
        super(context, R.layout.input_item, inputs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Input input = getItem(position);
        
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.input_item, parent,
                    false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tvValue);
        
        tvName.setText(input.getName());
        tvValue.setText(input.getValue());
        
        return convertView;
    }

}
