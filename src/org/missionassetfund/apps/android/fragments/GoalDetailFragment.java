package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class GoalDetailFragment extends Fragment implements View.OnClickListener {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_goal, container, false);
    setHasOptionsMenu(true);
    setupListeners();
    return view;
  }

  private void setupListeners() {
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.dashboard, menu);
  }

  @Override
  public void onClick(View v) {
  }
}
