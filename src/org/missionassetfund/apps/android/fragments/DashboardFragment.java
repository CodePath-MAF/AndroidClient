
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;
import org.missionassetfund.apps.android.models.CashSpentChart;
import org.missionassetfund.apps.android.models.MainDashboard;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

public class DashboardFragment extends Fragment {
    public static final int ADD_TRANSACTION_REQUEST_CODE = 1;

    private ImageButton btnAddTransaction;
    private RelativeLayout rlMonthlySpentChart;
    private LinearLayout llMonthlySpentChartProgress;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        setupViews(view);

        setupListeners();

        showMonthlySpentChartProgressBar();

        ParseCloud.callFunctionInBackground("dashboardView", new HashMap<String, Object>(),
                new FunctionCallback<HashMap<String, Object>>() {

                    @Override
                    public void done(HashMap<String, Object> result, ParseException exception) {
                        // TODO: Handle case of no data points to plot

                        final ObjectMapper mapper = new ObjectMapper();
                        mapper.setSerializationInclusion(Include.NON_NULL);

                        Log.d("DEBUG", result.toString());

                        final MainDashboard mainDashboardData = mapper.convertValue(result,
                                MainDashboard.class);
                        final CashSpentChart cashSpentChart = mainDashboardData.getCashSpentChart();

                        BigDecimal totalCash = mainDashboardData.getTotalCash();

                        // Update total cash on Action Bar
                        getActivity().setTitle(
                                getResources().getString(
                                        R.string.dashboard_title_with_total_cash,
                                        CurrencyUtils.getCurrencyValueFormatted(totalCash)));

                        List<BigDecimal> data = cashSpentChart.getData();
                        List<String> xLabels = cashSpentChart.getxLabels();

                        Log.d("DEBUG", "Data points: " + data.toString());
                        Log.d("DEBUG", "xLabels: " + xLabels.toString());
                        Log.d("DEBUG", "# of goals: "
                                + String.valueOf(mainDashboardData.getGoals().size()));

                        setupChart(rlMonthlySpentChart, data, xLabels);
                    }
                });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard, menu);
    }

    private void setupViews(View v) {
        rlMonthlySpentChart = (RelativeLayout) v.findViewById(R.id.rlMonthlySpentChart);
        btnAddTransaction = (ImageButton) v.findViewById(R.id.btnAddTransaction);
        llMonthlySpentChartProgress = (LinearLayout) v
                .findViewById(R.id.llMonthlySpentChartProgress);
    }

    private void setupListeners() {
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

    private void setupChart(RelativeLayout rlMonthlySpent, List<BigDecimal> data,
            List<String> xLabels) {

        String[] xDates = xLabels.toArray(new String[xLabels.size()]);

        // Creating an XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("Expense");

        // Adding data to Expense Series
        for (int i = 0; i < data.size(); i++) {
            expenseSeries.add(i, data.get(i).doubleValue());
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Expense Series to dataset
        dataset.addSeries(expenseSeries);

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(getResources().getColor(R.color.black));
        // TODO: Add a custom PointStyle like the mocks
        expenseRenderer.setPointStyle(PointStyle.CIRCLE);
        expenseRenderer.setLineWidth(getResources().getDimension(R.dimen.spent_chart_line));
        expenseRenderer.setPointStrokeWidth(getResources().getDimension(R.dimen.spent_chart_point));
        expenseRenderer.setShowLegendItem(false);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setBackgroundColor(getResources().getColor(R.color.transparent));
        multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent));
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setShowGridY(false);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYLabelsColor(0,
                getResources().getColor(R.color.dashboard_cash_spent_chart_ylabel_text));
        multiRenderer.setXLabelsColor(getResources().getColor(R.color.dashboard_cash_spent_chart_xlabel_text));
        multiRenderer.setLabelsTextSize(getResources().getDimension(
                R.dimen.spent_chart_labels_text_size));
        multiRenderer.setClickEnabled(true);
        multiRenderer.setMargins(new int[] {
                getResources().getDimensionPixelOffset(R.dimen.spent_chart_margin_top),
                getResources().getDimensionPixelOffset(R.dimen.spent_chart_margin_left),
                getResources().getDimensionPixelOffset(R.dimen.spent_chart_margin_bottom),
                getResources().getDimensionPixelOffset(R.dimen.spent_chart_margin_right)
        });
        multiRenderer.setYLabelsAlign(Align.RIGHT);
        multiRenderer.setYLabelsPadding(getResources().getDimensionPixelOffset(
                R.dimen.spent_chart_labels_padding));
        multiRenderer.setXLabelsAlign(Align.CENTER);
        multiRenderer.setXLabelsPadding(getResources().getDimensionPixelOffset(
                R.dimen.spent_chart_labels_padding));

        // Sample customization leaving as comment in case needed
        // multiRenderer.setChartTitle("Cash spent");
        // multiRenderer.setXTitle("Weeks");
        // multiRenderer.setYTitle("$");

        // Add custom label
        for (int i = 0; i < xDates.length; i++) {
            multiRenderer.addXTextLabel(i, xDates[i]);
        }

        // Adding expenseRenderer to multipleRenderer
        multiRenderer.addSeriesRenderer(expenseRenderer);

        // Creating graphicView to add to the RelativeLayout
        GraphicalView graphicalView = ChartFactory.getLineChartView(getActivity(),
                dataset, multiRenderer);

        // Set OnClickListener
        graphicalView.setOnClickListener(liquidAssetClickListener);

        // Hide progress Bar
        hideMonthlySpentChartProgressBar();

        rlMonthlySpent.addView(graphicalView);
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
            // TODO: refresh chart and cash
        }
    }

    private void showMonthlySpentChartProgressBar() {
        llMonthlySpentChartProgress.setVisibility(View.VISIBLE);
    }

    private void hideMonthlySpentChartProgressBar() {
        llMonthlySpentChartProgress.setVisibility(View.GONE);
    }
}
