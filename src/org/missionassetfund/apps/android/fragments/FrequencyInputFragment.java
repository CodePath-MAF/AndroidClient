
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.FrequencyAdapter;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;
import org.missionassetfund.apps.android.models.GoalPaymentInterval;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class FrequencyInputFragment extends Fragment {
    private OnInputFormListener onInputFormListener;

    private Spinner spFrequency;
    private ImageButton btnBack;
    private ImageButton btnNext;

    private FrequencyAdapter frequencyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frequency_input, container, false);

        // Setup view
        spFrequency = (Spinner) view.findViewById(R.id.spFrequency);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);

        // Populate spinner
        frequencyAdapter = new FrequencyAdapter(getActivity(), GoalPaymentInterval.values());

        spFrequency.setAdapter(frequencyAdapter);

        // Setup listener
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO(jose): Have next button disable until spFrequency has
                // value
                if (spFrequency.getSelectedItem() != null) {
                    GoalPaymentInterval frequency = (GoalPaymentInterval) spFrequency
                            .getSelectedItem();
                    onInputFormListener.OnNextSelected(FrequencyInputFragment.class,
                            frequency.toString());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_category_require),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onInputFormListener.OnBackSelected(FrequencyInputFragment.class);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnInputFormListener) {
            onInputFormListener = (OnInputFormListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnInputFormListener");
        }
    }

    public GoalPaymentInterval getFrequencySelected() {
        return (GoalPaymentInterval) spFrequency.getSelectedItem();
    }
}
