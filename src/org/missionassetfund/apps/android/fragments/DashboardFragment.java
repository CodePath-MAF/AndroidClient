
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

public class DashboardFragment extends Fragment {
    public static final int ADD_TRANSACTION_REQUEST_CODE = 1;

    private RelativeLayout rlLiquidAsset;
    private ImageButton btnAddTransaction;
    private LineGraph lgMonthlySpent;


    public interface SwitchMainFragmentListener {
        void SwitchToFragment(Class<? extends Fragment> klass);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.dashboard_title);

        // TODO(jose): Load data from Cloud Code
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        setupViews(view);

        setupListeners();

        setupUserData();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard, menu);
    }

    private void setupViews(View v) {
        rlLiquidAsset = (RelativeLayout) v.findViewById(R.id.rlLiquidAsset);
        btnAddTransaction = (ImageButton) v.findViewById(R.id.btnAddTransaction);
        lgMonthlySpent = (LineGraph) v.findViewById(R.id.lgMonthlySpent);

    }

    private void setupListeners() {
        rlLiquidAsset.setOnClickListener(liquidAssetClickListener);

        btnAddTransaction.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addTransactionIntent = new Intent(getActivity(),
                        AddTransactionActivity.class);
                startActivityForResult(addTransactionIntent, ADD_TRANSACTION_REQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
    }

    private void setupUserData() {
        // Sample Line Chart for mock purposes
        Line l = new Line();
        LinePoint p = new LinePoint();
        p.setX(0);
        p.setY(5);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(4);
        p.setY(8);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(8);
        p.setY(4);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(12);
        p.setY(5);
        l.addPoint(p);
        l.setColor(Color.BLACK);

        lgMonthlySpent.addLine(l);
        lgMonthlySpent.setRangeY(0, 12);
        lgMonthlySpent.setLineToFill(0);
    }

    private OnClickListener liquidAssetClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), LiquidAssetsActivity.class);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    };

    private void refreshGoalList() {
        GoalsListFragment fragmentGoalList = (GoalsListFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.goalListFragment);
        fragmentGoalList.updateGoalList();
    }

    @Override
    public void onResume() {
        refreshGoalList();
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == FragmentActivity.RESULT_OK && requestCode == ADD_TRANSACTION_REQUEST_CODE) {
            Toast.makeText(getActivity(), getString(R.string.parse_success_transaction_save),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
