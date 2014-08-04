package org.missionassetfund.apps.android.adapters;

import android.content.Context;

import com.sababado.circularview.Marker;
import com.sababado.circularview.SimpleCircularViewAdapter;

public class ProfileCircularViewAdapter extends SimpleCircularViewAdapter {
    private Context mContext;
    private int mTotalPeopleOnCircle;
    
    public ProfileCircularViewAdapter(Context context, int totalPeopleOnCircle) {
        this.mContext = context;
        this.mTotalPeopleOnCircle = totalPeopleOnCircle;
    }
    
    @Override
    public int getCount() {
        return mTotalPeopleOnCircle;
    }

    @Override
    public void setupMarker(final int position, final Marker marker) {
        int profileId = mContext.getResources().getIdentifier(String.format("profile_%s", position + 1), "drawable", mContext.getPackageName());
        
        marker.setSrc(profileId);
        marker.setRadius(70f);
    }

}
