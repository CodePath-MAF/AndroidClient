
package org.missionassetfund.apps.android.fragments;

import java.util.Date;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.Transaction.TransactionType;
import org.missionassetfund.apps.android.models.User;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class InitialSetupFragment extends Fragment {

    private OnInitialSetupListener listener;

    private EditText etAmount;
    private ImageButton btnNext;

    public interface OnInitialSetupListener {
        public void onSuccessfulSetup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_setup, container, false);

        // Change title
        getActivity().setTitle(R.string.initial_setup_title);

        // Setup view
        etAmount = (EditText) view.findViewById(R.id.etAmount);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);

        // Setup listener
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!etAmount.getText().toString().isEmpty()) {
                    getActivity().setProgressBarIndeterminateVisibility(true);

                    Double amount = Double.parseDouble(etAmount.getText().toString());
                    // Setting up a category referene for "Bills" category
                    Category categoryReference = ParseObject.createWithoutData(Category.class,
                            "93BaEoZPfo");
                    Transaction transaction = new Transaction();
                    transaction.setAmount(amount);
                    transaction.setUser((User) User.getCurrentUser());
                    transaction.setTransactionDate(new Date());
                    transaction.setCategory(categoryReference);
                    transaction.setName(getString(R.string.initial_setup_transaction_name));
                    transaction.setType(TransactionType.DEBIT);
                    transaction.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(getActivity(),
                                        getString(R.string.parse_error_saving),
                                        Toast.LENGTH_LONG).show();
                                Log.d("DEBUG", e.getMessage());
                                // TODO: have a listener to report that the save
                                // process failed
                                getActivity().setProgressBarIndeterminateVisibility(false);
                            } else {
                                // TODO: Potentially move this to Parse Cloud
                                // Code
                                User currentUser = (User) User.getCurrentUser();
                                currentUser.setSetup(true);
                                currentUser.saveInBackground(new SaveCallback() {

                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Toast.makeText(getActivity(),
                                                    getString(R.string.parse_error_saving),
                                                    Toast.LENGTH_LONG).show();
                                            Log.d("DEBUG", e.getMessage());
                                            // TODO: have a listener to report
                                            // that the save process failed
                                            getActivity().setProgressBarIndeterminateVisibility(
                                                    false);
                                        } else {
                                            listener.onSuccessfulSetup();
                                        }
                                    }
                                });
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_amount_require),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnInitialSetupListener) {
            listener = (OnInitialSetupListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnInitialSetupListener");
        }
    }
}
