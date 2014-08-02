
package org.missionassetfund.apps.android.fragments;

import org.lucasr.twowayview.TwoWayView;
import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.GoalDetailsActivity;
import org.missionassetfund.apps.android.adapters.GoalAdapter;
import org.missionassetfund.apps.android.models.Goal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class GoalsListFragment extends Fragment {

    private TwoWayView lvGoals;
    private GoalAdapter goalAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        goalAdapter = new GoalAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goals_list, container, false);

        lvGoals = (TwoWayView) v.findViewById(R.id.lvGoals);
        lvGoals.setAdapter(goalAdapter);
        goalAdapter.loadObjects();

        setupListeners();

        return v;
    }

    private void setupListeners() {
        lvGoals.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
                Goal goal = (Goal) adapter.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), GoalDetailsActivity.class);
                intent.putExtra(Goal.GOAL_KEY, goal.getObjectId());
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
    }

    public void updateGoalList() {
        goalAdapter.loadObjects();
    }
}
