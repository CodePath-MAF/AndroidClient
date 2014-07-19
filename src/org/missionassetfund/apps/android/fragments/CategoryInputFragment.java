
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.CategoryAdapter;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;
import org.missionassetfund.apps.android.models.Category;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;

public class CategoryInputFragment extends Fragment {

    private OnInputFormListener onInputFormListener;

    private Spinner spType;
    private ImageButton btnBack;
    private ImageButton btnNext;

    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_input, container, false);

        // Setup view
        spType = (Spinner) view.findViewById(R.id.spType);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);

        // populate spinner data
        categoryAdapter = new CategoryAdapter(getActivity());
        //categoryAdapter.setTextKey(Category.NAME_KEY);
        spType.setAdapter(categoryAdapter);
        categoryAdapter.loadObjects();

        // Setup listener
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Category c = (Category) spType.getSelectedItem();
                onInputFormListener.OnNextSelected(CategoryInputFragment.class, c.getName());
            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onInputFormListener.OnBackSelected(CategoryInputFragment.class);
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
