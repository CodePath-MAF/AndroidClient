
package org.missionassetfund.apps.android.adapters;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CategoryAdapter extends ParseQueryAdapter<Category> implements SpinnerAdapter {

    public CategoryAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Category>() {

            @Override
            public ParseQuery<Category> create() {
                ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
                return query;
            }
        });
    }

    @Override
    public View getItemView(Category category, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_category, null);
        }

        super.getItemView(category, v, parent);

        // Look up view elements
        TextView tvCategoryName = (TextView) v.findViewById(R.id.tvCategoryName);

        tvCategoryName.setText(category.getName());

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);

        View v;

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            v = inflator.inflate(R.layout.item_category_dropdown, parent, false);
        } else {
            v = convertView;
        }

        TextView tvCategoryName = (TextView) v.findViewById(R.id.tvCategoryName);
        tvCategoryName.setText(category.getName());

        return v;
    }
}
