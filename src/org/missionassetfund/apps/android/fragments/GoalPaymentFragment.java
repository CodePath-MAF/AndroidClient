
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.User;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GoalPaymentFragment extends DialogFragment {

    EditText etAmount;
    Button btnGoalPayment;

    public static GoalPaymentFragment newInstance() {
        GoalPaymentFragment goalPaymentFragment = new GoalPaymentFragment();
        return goalPaymentFragment;
    }

    public GoalPaymentFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.NewGoalDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_payment, container);
        btnGoalPayment = (Button) view.findViewById(R.id.btnGoalPayment);
        etAmount = (EditText) view.findViewById(R.id.etAmount);
        btnGoalPayment.setOnClickListener(newGoalPaymentListener);

        return view;
    }

    private OnClickListener newGoalPaymentListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // Goal goal = new Goal();
            // User user = (User) ParseUser.getCurrentUser();
            // goal.setUser(user);
            // Float amount = Float.parseFloat(etAmount.getText().toString());
            // goal.setAmount(amount);
            // goal.saveInBackground(new SaveCallback() {
            //
            // @Override
            // public void done(ParseException arg0) {
            // Toast.makeText(getActivity(), "Done saving goal.",
            // Toast.LENGTH_SHORT).show();
            // dismiss();
            //
            // }
            //
            // });
        }
    };

}
