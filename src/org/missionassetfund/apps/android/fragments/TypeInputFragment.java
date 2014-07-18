
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;
import org.missionassetfund.apps.android.models.Category;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.parse.ParseQueryAdapter;

public class TypeInputFragment extends Fragment {

    private OnInputFormListener onInputFormListener;

    private Spinner spType;
    private Button btnBack;
    private Button btnNext;

    private ParseQueryAdapter<Category> categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_input, container, false);

        // Setup view
        spType = (Spinner) view.findViewById(R.id.spType);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnNext = (Button) view.findViewById(R.id.btnNext);

        // populate spinner data
        categoryAdapter = new ParseQueryAdapter<Category>(getActivity(), Category.class);
        categoryAdapter.setTextKey(Category.NAME_KEY);
        spType.setAdapter(categoryAdapter);

        // Setup listener
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Category c = (Category) spType.getSelectedItem();
                onInputFormListener.OnNextSelected(TypeInputFragment.class, c.getName());
            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onInputFormListener.OnBackSelected(TypeInputFragment.class);
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

    public Category getCategorySelected() {
        return (Category) spType.getSelectedItem();
    }
}
