
package org.missionassetfund.apps.android.fragments;

import java.util.List;

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
import android.widget.Toast;

import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class CategoryInputFragment extends Fragment {

    private OnInputFormListener onInputFormListener;

    private Spinner spType;
    private ImageButton btnBack;
    private ImageButton btnNext;

    private CategoryAdapter categoryAdapter;
    private String mCategoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_input, container, false);

        // Setup view
        spType = (Spinner) view.findViewById(R.id.spType);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);

        // populate spinner data
        categoryAdapter = new CategoryAdapter(getActivity());
        categoryAdapter.setAutoload(false);
        categoryAdapter.addOnQueryLoadListener(mCategoryListener);
        categoryAdapter.loadObjects();
        spType.setAdapter(categoryAdapter);

        // Setup listener
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO(jose): Have next button disable until spType has value
                if (spType.getSelectedItem() != null) {
                    Category c = (Category) spType.getSelectedItem();
                    onInputFormListener.OnNextSelected(CategoryInputFragment.class, c.getName());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_category_require),
                            Toast.LENGTH_SHORT).show();
                }
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

    private OnQueryLoadListener<Category> mCategoryListener = new OnQueryLoadListener<Category>() {

        @Override
        public void onLoaded(List<Category> categories, Exception exception) {
            if (mCategoryId != null) {
                int position = findCategoryPosition(mCategoryId, categories);
                spType.setSelection(position);
            }

        }

        @Override
        public void onLoading() {
        }

    };

    private int findCategoryPosition(String categoryId, List<Category> categories) {
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);

            if (category.getObjectId().equals(categoryId)) {
                return i;
            }
        }

        return 0;
    }
}
