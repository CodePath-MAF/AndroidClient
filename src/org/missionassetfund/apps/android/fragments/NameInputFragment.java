
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
import android.widget.Toast;

public class NameInputFragment extends Fragment {

    private OnInputFormListener onInputFormListener;
    private OnCreateViewListener onCreateViewListener;

    private EditText etName;
    private ImageButton btnBack;
    private ImageButton btnNext;

    public interface OnCreateViewListener {
        public void setEditTextName(EditText editTextName);
    }

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
                // TODO(jose): Have next button disable until etName has value
                if (!etName.getText().toString().isEmpty()) {
                    onInputFormListener.OnNextSelected(NameInputFragment.class, etName.getText()
                            .toString());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_name_require),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onInputFormListener.OnBackSelected(NameInputFragment.class);
            }
        });

        onCreateViewListener.setEditTextName(etName);

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

        if (activity instanceof OnCreateViewListener) {
            onCreateViewListener = (OnCreateViewListener) activity;
        }
    }

    public String getNameSelected() {
        return etName.getText().toString();
    }
}
