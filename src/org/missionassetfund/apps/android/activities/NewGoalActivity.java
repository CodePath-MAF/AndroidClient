
package org.missionassetfund.apps.android.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.GoalPaymentInterval;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.MAFDateUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewGoalActivity extends Activity {

    private EditText etGoalName;
    private EditText etGoalAmount;
    private Spinner spinnerGoalFrequency;
    private EditText etGoalDate;
    private Button btnCreateGoal;

    private ArrayAdapter<GoalPaymentInterval> frequencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        frequencyAdapter = new ArrayAdapter<GoalPaymentInterval>(this,
                android.R.layout.simple_spinner_item, GoalPaymentInterval.values());
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setupViews();
    }

    private void setupViews() {
        etGoalName = (EditText) findViewById(R.id.etNewGoalName);
        btnCreateGoal = (Button) findViewById(R.id.btnCreateGoal);
        etGoalAmount = (EditText) findViewById(R.id.etGoalAmount);
        spinnerGoalFrequency = (Spinner) findViewById(R.id.spinnerGoalFrequency);
        etGoalDate = (EditText) findViewById(R.id.etGoalDate);

        spinnerGoalFrequency.setAdapter(frequencyAdapter);

        btnCreateGoal.setOnClickListener(newGoalClickListener);
    }

    private OnClickListener newGoalClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Goal goal = new Goal();
            goal.setUser((User) ParseUser.getCurrentUser());
            goal.setName(etGoalName.getText().toString());
            Double amount = Double.parseDouble(etGoalAmount.getText().toString());
            goal.setAmount(amount);
            GoalPaymentInterval paymentInterval = (GoalPaymentInterval) spinnerGoalFrequency
                    .getSelectedItem();
            goal.setPaymenyInterval(paymentInterval);

            // This will be replaced by date picker. Bear it for now.
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            // Default is 2 months after current date
            Date goalDate = MAFDateUtils.addDaysToDate(new Date(), 60);
            try {
                goalDate = dateFormat.parse(etGoalDate.getText().toString());
            } catch (java.text.ParseException e) {
                Log.e("error", getString(R.string.error_parse_date), e);
            }
            goal.setGoalDate(goalDate);

            int numDaysToTargetDate = MAFDateUtils.getDaysTo(goalDate);
            Integer numPayments = numDaysToTargetDate / paymentInterval.toInt();
            goal.setNumPayments(numPayments);

            goal.setPaymentAmount(amount / numPayments);

            goal.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException arg0) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.toast_done_saving_goal), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }

            });
        }
    };
}
