
package org.missionassetfund.apps.android.activities;

import java.util.ArrayList;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.InputItemAdapter;
import org.missionassetfund.apps.android.fragments.AmountInputFragment;
import org.missionassetfund.apps.android.fragments.CategoryInputFragment;
import org.missionassetfund.apps.android.fragments.DateInputFragment;
import org.missionassetfund.apps.android.fragments.DoneFragment;
import org.missionassetfund.apps.android.fragments.NameInputFragment;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;
import org.missionassetfund.apps.android.models.Input;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.Transaction.TransactionType;
import org.missionassetfund.apps.android.models.User;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

public class AddTransactionActivity extends FragmentActivity
        implements OnInputFormListener, NameInputFragment.OnCreateViewListener,
        CategoryInputFragment.OnCreateViewListener, AmountInputFragment.OnCreateViewListener {

    private ListView lvSteps;
    private ArrayList<Input> inputs;
    private InputItemAdapter aInput;

    private Input[] inputElements;
    private String mCategoryId;
    private String mTransactionName;

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
                new Input("Amount", "0", 0, AmountInputFragment.class),
                new Input("Name", "Lunch", 1, NameInputFragment.class),
                new Input("Category", "Food", 2, CategoryInputFragment.class),
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
        @SuppressWarnings("unchecked")
        Input input = inputElements[getInputPosition(activeFragmentClass)];
        input.setValue(value);
        inputs.add(input);
        aInput.notifyDataSetChanged();
        hideSoftKeyboard();
        showFragment(getNextFragmentClass(input));
    }

    private void setupDataFromIntent() {
        mCategoryId = getIntent().getStringExtra("category_id");
        mTransactionName = getIntent().getStringExtra("transaction_name");
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
        CategoryInputFragment typeFragment = (CategoryInputFragment) mgr
                .findFragmentByTag(CategoryInputFragment.class.getName());
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

    private Class<?> getNextFragmentClass(Input input) {
        return inputElements[input.getPos() + 1].getFragmentClass();
    }

    private Class<?> getPreviousFragmentClass(Input input) {
        return inputElements[input.getPos() - 1].getFragmentClass();
    }

    @Override
    public void setEditTextName(EditText editTextName) {
        if (mTransactionName != null) {
            editTextName.setText(mTransactionName);
        }
        editTextName.setHint(R.string.smaple_new_transaction_name);
    }

    @Override
    public String getCategoryId() {
        return mCategoryId == null ? null : mCategoryId;
    }

    @Override
    public void setAmountCategoryVisibility(RadioGroup rgType) {
        rgType.setVisibility(View.VISIBLE);
    }
}
