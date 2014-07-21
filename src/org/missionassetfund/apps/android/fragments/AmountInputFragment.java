
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.interfaces.OnInputFormListener;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AmountInputFragment extends Fragment {

    private OnInputFormListener listener;
    private OnCreateViewListener onCreateViewListener;

    private EditText etAmount;
    private RadioButton rbExpense;
    private ImageButton btnNext;
    private RadioGroup rgType;

    public interface OnCreateViewListener {
        public void setAmountCategoryVisibility(RadioGroup rgType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amount_input, container, false);

        // Setup view
        etAmount = (EditText) view.findViewById(R.id.etAmount);
        rbExpense = (RadioButton) view.findViewById(R.id.rbExpense);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        rgType = (RadioGroup) view.findViewById(R.id.rgType);

        // Setup listener
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO(jose): Have next button disable until etAmount has value
                if (!etAmount.getText().toString().isEmpty()) {
                    Double amount = Double.parseDouble(etAmount.getText().toString());
                    listener.OnNextSelected(AmountInputFragment.class,
                            CurrencyUtils.getCurrencyValueFormatted(CurrencyUtils
                                    .newCurrency(amount)));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_amount_require),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        onCreateViewListener.setAmountCategoryVisibility(rgType);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnInputFormListener) {
            listener = (OnInputFormListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnInputFormListener");
        }

        if (activity instanceof OnCreateViewListener) {
            onCreateViewListener = (OnCreateViewListener) activity;
        }
    }

    public String getAmountSelected() {
        return etAmount.getText().toString();
    }

    public boolean isExpenseTransaction() {
        return rbExpense.isChecked() ? true : false;
    }

}
