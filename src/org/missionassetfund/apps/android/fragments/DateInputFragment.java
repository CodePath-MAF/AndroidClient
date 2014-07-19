
package org.missionassetfund.apps.android.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;
import org.missionassetfund.apps.android.utils.MAFDateUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageButton;

public class DateInputFragment extends Fragment {

    private OnInputFormListener onInputFormListener;

    private DatePicker dpDate;
    private ImageButton btnBack;
    private ImageButton btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_input, container, false);

        // Setup view
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);

        // TODO(jose): style datepicker
        dpDate = (DatePicker) view.findViewById(R.id.dpDate);
        dpDate.setCalendarViewShown(false);

        // initialize the date to current date
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dateStr = sdfDateTime.format(new Date(System.currentTimeMillis()));

        String[] dateSplit = dateStr.split("-");
        int currentYear = Integer.parseInt(dateSplit[0]);
        int currentMonth = Integer.parseInt(dateSplit[1]);
        int currentDay = Integer.parseInt(dateSplit[2]);

        dpDate.init(currentYear, currentMonth - 1, currentDay, new OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            }
        });

        // Setup listener
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(dpDate.getYear(), dpDate.getMonth(), dpDate.getDayOfMonth());
                dpDate.setTag(newDate.getTime());
                onInputFormListener.OnNextSelected(DateInputFragment.class,
                        MAFDateUtils.getRelativeDate(newDate.getTime()));
            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onInputFormListener.OnBackSelected(DateInputFragment.class);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnInputFormListener) {
            onInputFormListener = (OnInputFormListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnInputFormListener");
        }
    }
    
    public Date getDateSelected() {
        return (Date) dpDate.getTag();
    }
}
