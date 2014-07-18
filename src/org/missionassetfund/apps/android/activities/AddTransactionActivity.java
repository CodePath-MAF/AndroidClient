
package org.missionassetfund.apps.android.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.InputItemAdapter;
import org.missionassetfund.apps.android.fragments.AmountInputFragment;
import org.missionassetfund.apps.android.fragments.DateInputFragment;
import org.missionassetfund.apps.android.fragments.DoneFragment;
import org.missionassetfund.apps.android.fragments.NameInputFragment;
import org.missionassetfund.apps.android.fragments.TypeInputFragment;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;
import org.missionassetfund.apps.android.models.Input;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.Transaction.TransactionType;
import org.missionassetfund.apps.android.models.User;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

public class AddTransactionActivity extends FragmentActivity implements OnInputFormListener {

    private ListView lvSteps;
    private ArrayList<Input> inputs;
    private InputItemAdapter aInput;

    private HashMap<Class<? extends Fragment>, Input> inputElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // TODO(jose): clicking an item on the ListView should pop up the fragment to edit.
        lvSteps = (ListView) findViewById(R.id.lvSteps);

        inputs = new ArrayList<Input>();
        aInput = new InputItemAdapter(this, inputs);

        // setup input steps
        inputElements = new HashMap<Class<? extends Fragment>, Input>();
        inputElements.put(AmountInputFragment.class, new Input("Amount", 0, null,
                NameInputFragment.class));
        inputElements.put(NameInputFragment.class, new Input("Name", 1, AmountInputFragment.class,
                TypeInputFragment.class));
        inputElements.put(TypeInputFragment.class, new Input("Category", 2,
                NameInputFragment.class, DateInputFragment.class));
        inputElements.put(DateInputFragment.class, new Input("Date", 3, TypeInputFragment.class,
                DoneFragment.class));
        inputElements.put(DoneFragment.class, new Input("Done", 4, DateInputFragment.class, null));

        lvSteps.setAdapter(aInput);

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
        Class[] fragmentClasses = new Class[] {
                AmountInputFragment.class,
                NameInputFragment.class,
                TypeInputFragment.class,
                DateInputFragment.class,
                DoneFragment.class
        };
        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction();
        try {
            for (Class klass : fragmentClasses) {
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

    @Override
    @SuppressWarnings("rawtypes")
    public void OnNextSelected(Class activeFragmentClass, String value) {
        Input input = inputElements.get(activeFragmentClass);
        input.setValue(value);
        inputs.add(input);
        aInput.notifyDataSetChanged();
        showFragment(input.getNextFragment());
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void OnBackSelected(Class activeFragmentClass) {
        Input input = inputElements.get(activeFragmentClass);
        inputs.remove(input.getPos() - 1);
        aInput.notifyDataSetChanged();
        showFragment(input.getPreviousFragment());
    }

    @Override
    public void OnFinishSelected() {
        FragmentManager mgr = getSupportFragmentManager();

        // TODO(jose): Data validation
        Transaction transaction = new Transaction();
        transaction.setUser((User) User.getCurrentUser());

        // Get Amount data from fragment
        AmountInputFragment amountFragment = (AmountInputFragment) mgr
                .findFragmentByTag(AmountInputFragment.class.getName());
        transaction.setAmount(Double.parseDouble(amountFragment.getAmountSelected()));

        // Get Name data from fragment
        NameInputFragment nameFragment = (NameInputFragment) mgr
                .findFragmentByTag(NameInputFragment.class.getName());
        transaction.setName(nameFragment.getNameSelected());

        // Get Category selected from fragment
        TypeInputFragment typeFragment = (TypeInputFragment) mgr
                .findFragmentByTag(TypeInputFragment.class.getName());
        transaction.setCategory(typeFragment.getCategorySelected());

        // Get Date from fragment
        DateInputFragment dateFragment = (DateInputFragment) mgr
                .findFragmentByTag(DateInputFragment.class.getName());
        transaction.setTransactionDate(dateFragment.getDateSelected());

        transaction.setType(amountFragment.isExpenseTransaction() ? TransactionType.CREDIT
                : TransactionType.DEBIT);

        transaction.saveInBackground(new SaveCallback() {

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
}
