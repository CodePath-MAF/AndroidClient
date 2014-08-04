package org.missionassetfund.apps.android.adapters;

import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.CashOutSchedule;

import android.content.Context;

import com.sababado.circularview.Marker;
import com.sababado.circularview.SimpleCircularViewAdapter;

public class ProfileCircularViewAdapter extends SimpleCircularViewAdapter {
    private Context mContext;
    private List<CashOutSchedule> mCashOutSchedule;
    
    public ProfileCircularViewAdapter(Context context, List<CashOutSchedule> cashOutSchedule) {
        this.mContext = context;
        this.mCashOutSchedule = cashOutSchedule;
    }
    
    @Override
    public int getCount() {
        return mCashOutSchedule.size();
    }

    @Override
    public void setupMarker(final int position, final Marker marker) {
        Integer profileImageId = mCashOutSchedule.get(position).getProfileImageId();
        int profileId = mContext.getResources().getIdentifier(String.format("profile_%s", profileImageId), "drawable", mContext.getPackageName());
        
        marker.setSrc(profileId);
        marker.setRadius(mContext.getResources().getDimension(R.dimen.profile_icon_size));
    }

}
