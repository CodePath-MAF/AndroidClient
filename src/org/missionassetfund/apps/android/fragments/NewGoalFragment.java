
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

public class NewGoalFragment extends DialogFragment {

    EditText etGoalName;
    EditText etAmount;
    Button btnCreateGoal;

    public NewGoalFragment() {
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
        View view = inflater.inflate(R.layout.fragment_new_goal, container);
        etGoalName = (EditText) view.findViewById(R.id.etNewGoalName);
        btnCreateGoal = (Button) view.findViewById(R.id.btnCreateGoal);
        etAmount = (EditText) view.findViewById(R.id.etAmount);
        btnCreateGoal.setOnClickListener(newGoalClickListener);

        return view;
    }

    private OnClickListener newGoalClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Goal goal = new Goal();
            User user = (User) ParseUser.getCurrentUser();
            goal.setUser(user);
            goal.setName(etGoalName.getText().toString());
            Float amount = Float.parseFloat(etAmount.getText().toString());
            goal.setAmount(amount);
            goal.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException arg0) {
                    Toast.makeText(getActivity(), "Done saving goal.", Toast.LENGTH_SHORT).show();
                    dismiss();

                }

            });
        }
    };

}
