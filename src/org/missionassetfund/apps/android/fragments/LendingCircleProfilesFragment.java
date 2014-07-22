
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.LendingCircleFriend;
import org.missionassetfund.apps.android.models.LendingCircleFriends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LendingCircleProfilesFragment extends Fragment {

    private static final String START_ID_TAG = "start_id";

    int startId;

    public static final LendingCircleProfilesFragment newInstance(int startId)
    {
        LendingCircleProfilesFragment f = new LendingCircleProfilesFragment();
        Bundle bdl = new Bundle(1);
        bdl.putInt(START_ID_TAG, startId);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startId = getArguments().getInt(START_ID_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lending_circle, container, false);
        View profile1 = v.findViewById(R.id.profile1);
        View profile2 = v.findViewById(R.id.profile2);
        View profile3 = v.findViewById(R.id.profile3);
        View profile4 = v.findViewById(R.id.profile4);

        ImageView ivProfile1 = (ImageView) profile1.findViewById(R.id.ivFriend);
        TextView tvName1 = (TextView) profile1.findViewById(R.id.tvFriendName);
        LendingCircleFriend f1 = LendingCircleFriends.get(startId);
        ivProfile1.setImageResource(f1.getDpDrawableId());
        tvName1.setText(f1.getName());

        ImageView ivProfile2 = (ImageView) profile2.findViewById(R.id.ivFriend);
        TextView tvName2 = (TextView) profile2.findViewById(R.id.tvFriendName);
        LendingCircleFriend f2 = LendingCircleFriends.get(startId + 1);
        ivProfile2.setImageResource(f2.getDpDrawableId());
        tvName2.setText(f2.getName());

        ImageView ivProfile3 = (ImageView) profile3.findViewById(R.id.ivFriend);
        TextView tvName3 = (TextView) profile3.findViewById(R.id.tvFriendName);
        LendingCircleFriend f3 = LendingCircleFriends.get(startId + 2);
        ivProfile3.setImageResource(f3.getDpDrawableId());
        tvName3.setText(f3.getName());

        ImageView ivProfile4 = (ImageView) profile4.findViewById(R.id.ivFriend);
        TextView tvName4 = (TextView) profile4.findViewById(R.id.tvFriendName);
        LendingCircleFriend f4 = LendingCircleFriends.get(startId + 3);
        ivProfile4.setImageResource(f4.getDpDrawableId());
        tvName4.setText(f4.getName());

        ivProfile1.setImageResource(R.drawable.profile_2);

        setupListeners();

        return v;
    }

    private void setupListeners() {
    }

}
