package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DashboardFragment extends Fragment {

  private FragmentActivity myContext;
  LinearLayout llGoal;

  @Override
  public void onAttach(Activity activity) {
    myContext = (FragmentActivity) activity;
    super.onAttach(activity);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
    setHasOptionsMenu(true);
    llGoal = (LinearLayout) view.findViewById(R.id.llGoal);
    setupListeners();
    return view;
  }

  private void setupListeners() {
    llGoal.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // Toast.makeText(getActivity(), "Hi", Toast.LENGTH_LONG).show();
        FragmentManager mgr = myContext.getSupportFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction();
        transaction.replace(R.id.frmMainContent, new GoalFragment());
        transaction.commit();
      }
    });

  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.dashboard, menu);
  }

}
