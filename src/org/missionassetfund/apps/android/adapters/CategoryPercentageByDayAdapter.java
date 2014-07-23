
package org.missionassetfund.apps.android.adapters;

import java.math.BigDecimal;
import java.util.Map;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.utils.FormatterUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryPercentageByDayAdapter extends BaseAdapter {

    private Map<Category, BigDecimal> mData;
    private Category[] mKeys;
    private Context mContext;

    public CategoryPercentageByDayAdapter(Context context, Map<Category, BigDecimal> data) {
        mContext = context;
        mData = data;
        mKeys = data.keySet().toArray(new Category[data.size()]);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public BigDecimal getItem(int position) {
        return mData.get(mKeys[position]);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_percentage,
                    parent,
                    false);
        }

        TextView tvCategoryName = (TextView) convertView.findViewById(R.id.tvCategoryName);
        TextView tvCategoryPercentage = (TextView) convertView
                .findViewById(R.id.tvCategoryPercentage);

        Category category = mKeys[position];
        BigDecimal percentage = this.getItem(position);

        tvCategoryName.setText(category.getName());
        tvCategoryName.setTextColor(Color.parseColor(category.getColor()));

        tvCategoryPercentage.setText(FormatterUtils.formatPercentage(percentage));
        return convertView;
    }

}
