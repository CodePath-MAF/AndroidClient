
package org.missionassetfund.apps.android.fragments;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialogListener listener;

    public interface DatePickerDialogListener {
        void onDateSelected(Date date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    // Store the listener (activity) that will have events fired once the
    // fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DatePickerDialogListener) {
            listener = (DatePickerDialogListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement DatePickerDialogListener");
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, day);
        listener.onDateSelected(newDate.getTime());
    }
}
