
package org.missionassetfund.apps.android.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.fragments.DatePickerFragment;
import org.missionassetfund.apps.android.fragments.DatePickerFragment.DatePickerDialogListener;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.User;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

public class AddTransactionActivity extends FragmentActivity implements DatePickerDialogListener {
    // TODO(jose): find out where should we put Transaction Type

    private EditText etTransactionName;
    private Spinner sCategory;
    private EditText etAmount;
    private EditText etDate;

    private ParseQueryAdapter<Category> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setupViews();
        setupListeners();
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

    private void setupViews() {
        etTransactionName = (EditText) findViewById(R.id.etTransactionName);
        sCategory = (Spinner) findViewById(R.id.sCategory);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etDate = (EditText) findViewById(R.id.etDate);

        // Setup Parse Adapter to fill-up the spinner
        categoryAdapter = new ParseQueryAdapter<Category>(this, Category.class);
        categoryAdapter.setTextKey(Category.NAME_KEY);
        sCategory.setAdapter(categoryAdapter);
    }

    private void setupListeners() {
        // Setup DatePickerDialog
        etDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public void onDateSelected(Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        etDate.setTag(date);
        etDate.setText(formatter.format(date));
    }

    public void onAddTransaction(View v) {
        // TODO(jose): Data validation
        Transaction transaction = new Transaction();
        transaction.setUser((User) User.getCurrentUser());
        transaction.setDescription(etTransactionName.getText().toString());
        transaction.setCategory((Category) sCategory.getSelectedItem());
        transaction.setAmount(Double.parseDouble(etAmount.getText().toString()));
        transaction.setTransactionDate((Date) etDate.getTag());

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
