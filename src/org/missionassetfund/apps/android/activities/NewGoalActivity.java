
package org.missionassetfund.apps.android.activities;

import java.util.ArrayList;
import java.util.Date;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.InputItemAdapter;
import org.missionassetfund.apps.android.fragments.AmountInputFragment;
import org.missionassetfund.apps.android.fragments.DateInputFragment;
import org.missionassetfund.apps.android.fragments.DoneFragment;
import org.missionassetfund.apps.android.fragments.FrequencyInputFragment;
import org.missionassetfund.apps.android.fragments.NameInputFragment;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.GoalPaymentInterval;
import org.missionassetfund.apps.android.models.Input;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.MAFDateUtils;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

public class NewGoalActivity extends FragmentActivity implements OnInputFormListener {

    private ListView lvSteps;
    private ArrayList<Input> inputs;
    private InputItemAdapter aInput;

    private Input[] inputElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lvSteps = (ListView) findViewById(R.id.lvSteps);

        inputs = new ArrayList<Input>();
        aInput = new InputItemAdapter(this, inputs);

        // setup input steps
        inputElements = new Input[] {
                new Input("Amount", "0", 0, AmountInputFragment.class),
                new Input("Name", "Travel Fund", 1, NameInputFragment.class),
                new Input("Frequency", "Monthly", 2, FrequencyInputFragment.class),
                new Input("Date", "Today", 3, DateInputFragment.class),
                new Input("Done", "", 4, DoneFragment.class)
        };

        lvSteps.setAdapter(aInput);

        // setup listeners
        lvSteps.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Input input = inputs.get(position);
                int size = inputs.size();
                for (int i = (size - 1); i >= position; i--) {
                    inputs.remove(i);
                }
                aInput.notifyDataSetChanged();
                showFragment(input.getFragmentClass());
            }
        });

        showFragment(AmountInputFragment.class);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void OnNextSelected(Class activeFragmentClass, String value) {
        @SuppressWarnings("unchecked")
        Input input = inputElements[getInputPosition(activeFragmentClass)];
        input.setValue(value);
        inputs.add(input);
        aInput.notifyDataSetChanged();
        hideSoftKeyboard();
        showFragment(getNextFragmentClass(input));
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void OnBackSelected(Class activeFragmentClass) {
        @SuppressWarnings("unchecked")
        Input input = inputElements[getInputPosition(activeFragmentClass)];
        inputs.remove(input.getPos() - 1);
        aInput.notifyDataSetChanged();
        showFragment(getPreviousFragmentClass(input));
    }

    @Override
    public void OnFinishSelected() {
        FragmentManager mgr = getSupportFragmentManager();

        // Get Amount data from fragment
        AmountInputFragment amountFragment = (AmountInputFragment) mgr
                .findFragmentByTag(AmountInputFragment.class.getName());
        Double goalAmount = Double.parseDouble(amountFragment.getAmountSelected());

        // Get Name data from fragment
        NameInputFragment nameFragment = (NameInputFragment) mgr
                .findFragmentByTag(NameInputFragment.class.getName());
        String goalName = nameFragment.getNameSelected();

        // Get Frequency selected from fragment
        FrequencyInputFragment frequencyFragment = (FrequencyInputFragment) mgr
                .findFragmentByTag(FrequencyInputFragment.class.getName());
        GoalPaymentInterval paymentInterval = (GoalPaymentInterval) frequencyFragment
                .getFrequencySelected();

        // Get Date from fragment
        DateInputFragment dateFragment = (DateInputFragment) mgr
                .findFragmentByTag(DateInputFragment.class.getName());
        Date goalDate = dateFragment.getDateSelected();

        // Get Number of Payments
        int numDaysToTargetDate = MAFDateUtils.getDaysTo(goalDate);
        Integer numPayments = numDaysToTargetDate / paymentInterval.toInt();

        // TODO(jose): Data validation
        Goal goal = new Goal();
        goal.setUser((User) User.getCurrentUser());
        goal.setAmount(goalAmount);
        goal.setName(goalName);
        goal.setPaymenyInterval(paymentInterval);
        goal.setNumPayments(numPayments);
        goal.setPaymentAmount(goalAmount / numPayments);
        goal.setGoalDate(goalDate);

        goal.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getApplication(), getString(R.string.parse_error_saving),
                            Toast.LENGTH_LONG).show();
                    Log.d("DEBUG", e.getMessage());
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("rawtypes")
    private void showFragment(Class activeFragmentClass) {
        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction();
        try {
            for (Input input : inputElements) {
                Class klass = input.getFragmentClass();
                Fragment fragment = mgr.findFragmentByTag(klass.getName());
                if (klass == activeFragmentClass) {
                    if (fragment != null) {
                        transaction.show(fragment);
                    } else {
                        transaction.add(R.id.flStepsContainer, (Fragment) klass.newInstance(),
                                klass.getName());
                    }
                } else {
                    if (fragment != null) {
                        transaction.hide(fragment);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        transaction.commit();
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private int getInputPosition(Class<? extends Fragment> fragmentClass) {
        for (Input i : inputElements) {
            if (i.getFragmentClass() == fragmentClass) {
                return i.getPos();
            }
        }
        return 0;
    }

    private Class<?> getNextFragmentClass(Input input) {
        return inputElements[input.getPos() + 1].getFragmentClass();
    }

    private Class<?> getPreviousFragmentClass(Input input) {
        return inputElements[input.getPos() - 1].getFragmentClass();
    }
}
