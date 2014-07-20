
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LendingCircleProfilesFragment extends Fragment {

    public static final LendingCircleProfilesFragment newInstance(String message)
    {
        LendingCircleProfilesFragment f = new LendingCircleProfilesFragment();
        Bundle bdl = new Bundle(1);
        // bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lending_circle, container, false);

        setupListeners();

        return v;
    }

    private void setupListeners() {
    }

    public void updateGoalList() {
    }
}
