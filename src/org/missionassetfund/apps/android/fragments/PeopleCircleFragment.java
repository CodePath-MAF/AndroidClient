
package org.missionassetfund.apps.android.fragments;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.ProfileCircularViewAdapter;
import org.missionassetfund.apps.android.models.CashOutSchedule;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import com.lylc.widget.circularprogressbar.example.CircularProgressBar;
import com.sababado.circularview.CircularView;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PeopleCircleFragment extends Fragment {

    private CircularProgressBar cpbGoalDetail;
    private CircularView cvProfile;
    private BigDecimal mGoalAmount;
    private BigDecimal mGoalPaymentAmount;
    private List<CashOutSchedule> mCashOutSchedule;
    
    private OnCreateViewListener listener;
    
    public interface OnCreateViewListener {
        public void onSetupData(PeopleCircleFragment fragment);
    }
    
    public void setGoalAmount(BigDecimal goalAmount) {
        this.mGoalAmount = goalAmount;
    }

    public void setGoalPaymentAmount(BigDecimal goalPaymentAmount) {
        this.mGoalPaymentAmount = goalPaymentAmount;
    }

    public void setCashOutSchedule(List<CashOutSchedule> cashOutSchedule) {
        this.mCashOutSchedule = cashOutSchedule;
    }

    @Override
    public void onAttach(Activity activity) {
      super.onAttach(activity);
        if (activity instanceof OnCreateViewListener) {
          listener = (OnCreateViewListener) activity;
        } else {
          throw new ClassCastException(activity.toString()
              + " must implement PeopleCircleFragment.OnCreateViewListener");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_circle, container, false);
        listener.onSetupData(this);

        cpbGoalDetail = (CircularProgressBar)
                view.findViewById(R.id.cpbGoalDetail);
        cpbGoalDetail.setTitle(CurrencyUtils.getCurrencyValueFormatted(mGoalAmount));
        cpbGoalDetail.setSubTitle(String.format("%s CONTRIBUTED\nTHIS MONTH", 
                CurrencyUtils.getCurrencyValueFormatted(mGoalPaymentAmount)));
        cpbGoalDetail.setProgress(getProgress());
        cpbGoalDetail.animateProgressTo(0, getProgress(), null);

        ProfileCircularViewAdapter mAdapter = new ProfileCircularViewAdapter(view.getContext(), mCashOutSchedule);
        cvProfile = (CircularView) view.findViewById(R.id.cvProfile);
        cvProfile.animateHighlightedDegree(0, 360, 3000);
        cvProfile.setAdapter(mAdapter);

        return view;
    }

    private int getProgress() {
        return mGoalPaymentAmount.divide(mGoalAmount, 4, RoundingMode.UP)
                .multiply(CurrencyUtils.newCurrency(100d)).intValue();
    }
}
