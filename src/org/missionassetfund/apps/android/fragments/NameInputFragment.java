
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
import android.widget.EditText;
import android.widget.ImageButton;

public class NameInputFragment extends Fragment {

    private OnInputFormListener onInputFormListener;

    private EditText etName;
    private ImageButton btnBack;
    private ImageButton btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name_input, container, false);

        // Setup view
        etName = (EditText) view.findViewById(R.id.etName);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);

        // Setup listener
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onInputFormListener.OnNextSelected(NameInputFragment.class, etName.getText()
                        .toString());
            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onInputFormListener.OnBackSelected(NameInputFragment.class);
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

    public String getNameSelected() {
        return etName.getText().toString();
    }
}
