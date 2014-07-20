
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class DoneFragment extends Fragment {

    private OnInputFormListener onInputFormListener;

    private ImageButton btnBack;
    private ImageButton btnFinish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done, container, false);

        // Setup view
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnFinish = (ImageButton) view.findViewById(R.id.btnFinish);

        // Setup listener
        btnFinish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onInputFormListener.OnFinishSelected();
            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onInputFormListener.OnBackSelected(DoneFragment.class);
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
}
