
package org.missionassetfund.apps.android.activities;

import java.util.ArrayList;
import java.util.Date;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.InputItemAdapter;
import org.missionassetfund.apps.android.fragments.AmountInputFragment;
import org.missionassetfund.apps.android.fragments.CategoryInputFragment;
import org.missionassetfund.apps.android.fragments.NameInputFragment;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.Input;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.Transaction.TransactionType;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.FormatterUtils;
import org.missionassetfund.apps.android.utils.ParseUtils;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class AddGoalPaymentActivity extends BaseFragmentActivity
        implements OnInputFormListener, NameInputFragment.OnCreateViewListener,
        CategoryInputFragment.OnCreateViewListener, AmountInputFragment.OnCreateViewListener {

    Goal goal;

    private ListView lvSteps;
    private ArrayList<Input> inputs;
    private InputItemAdapter aInput;

    private Input[] inputElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lvSteps = (ListView) findViewById(R.id.lvSteps);

        inputs = new ArrayList<Input>();
        aInput = new InputItemAdapter(this, inputs);

        // setup input steps
        inputElements = new Input[] {
                new Input("Amount Due", "0", 0, AmountInputFragment.class),
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

        setupDataFromIntent();

        showFragment(AmountInputFragment.class);
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
                    // Change ActionBar title
                    setActionBarTitle(input);

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

    @Override
    @SuppressWarnings("rawtypes")
    public void OnNextSelected(Class activeFragmentClass, String value) {
        OnFinishSelected();
    }

    private void setupDataFromIntent() {
        String goalId = getIntent().getStringExtra(Goal.GOAL_KEY);

        // TODO (amit) : code duplication.
        // Goal was pinned when calling goal details activity.
        // Querying form local datastore.
        ParseQuery<Goal> query = ParseQuery.getQuery(Goal.class);
        query.fromLocalDatastore();

        query.getInBackground(goalId, new GetCallback<Goal>() {

            @Override
            public void done(Goal g, ParseException e) {
                if (e == null) {
                    goal = g;

                    Input input = inputElements[0];
                    input.setValue(FormatterUtils.formatAmount(goal.getPaymentAmount()));
                    inputs.add(input);
                    aInput.notifyDataSetChanged();

                } else {
                    Toast.makeText(AddGoalPaymentActivity.this, "Error getting goal",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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

        final Transaction transaction = new Transaction();
        transaction.setUser((User) User.getCurrentUser());

        // Get Amount data from fragment
        AmountInputFragment amountFragment = (AmountInputFragment) mgr
                .findFragmentByTag(AmountInputFragment.class.getName());
        transaction.setAmount(Double.parseDouble(amountFragment.getAmountSelected()));

        transaction.setName(getString(R.string.label_goal_payment));

        transaction.setGoal(goal);
        transaction.setTransactionDate(new Date());
        transaction.setType(TransactionType.CREDIT);

        transaction.pinInBackground(ParseUtils.PIN_CALLBACK);

        Intent txnData = new Intent();
        txnData.putExtra(Transaction.NAME_KEY, transaction.getObjectId());
        setResult(RESULT_OK, txnData);
        finish();

        // transaction.saveInBackground(new SaveCallback() {
        //
        // @Override
        // public void done(ParseException e) {
        // if (e == null) {
        // Toast.makeText(AddGoalPaymentActivity.this, "Done savign txn",
        // Toast.LENGTH_LONG).show();
        // Intent txnData = new Intent();
        // txnData.putExtra(Transaction.NAME_KEY, transaction);
        // setResult(RESULT_OK, txnData);
        // setResult(RESULT_OK, txnData);
        // finish();
        // } else {
        // Log.e("goal", e.getLocalizedMessage(), e);
        // Toast.makeText(AddGoalPaymentActivity.this,
        // R.string.parse_error_saving,
        // Toast.LENGTH_SHORT).show();
        // }
        // }
        // });

    }

    /**
     * Set Action Bar title base on the step
     * 
     * @param input
     */
    private void setActionBarTitle(Input input) {
        setTitle(input.getName());
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

    // private Class<?> getNextFragmentClass(Input input) {
    // return inputElements[input.getPos() + 1].getFragmentClass();
    // }

    private Class<?> getPreviousFragmentClass(Input input) {
        return inputElements[input.getPos() - 1].getFragmentClass();
    }

    @Override
    public void setEditTextName(EditText editTextName) {
        editTextName.setHint(R.string.label_goal_payment);
    }

    @Override
    public String getCategoryId() {
        return getString(R.string.label_goal_payment);
    }

    @Override
    public void setAmountCategoryVisibility(RadioGroup rgType) {
        rgType.setVisibility(View.GONE);
    }
}
