package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.User;

import com.parse.ParseUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ProfileFragment extends Fragment {
    
    public static final String PASSWORD_PLACEHOLDER = "p4sspl4ceh0lder";
    
    private User mLoggedUser;
    private EditText etUsername;
    private EditText etUserFullName;
    private EditText etUserPassword;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        etUsername = (EditText) view.findViewById(R.id.etUsername);
        etUserFullName = (EditText) view.findViewById(R.id.etUserFullName); 
        etUserPassword = (EditText) view.findViewById(R.id.etUserPassword);
        
        mLoggedUser = (User) ParseUser.getCurrentUser();
        
        etUsername.setText(mLoggedUser.getUsername());
        etUserFullName.setText(mLoggedUser.getName());
        etUserPassword.setText(PASSWORD_PLACEHOLDER);
        return view;
    }
    
    public User getModifiedUser() {
        boolean errors = false;
        String name = etUserFullName.getText().toString();
        
        if (name.isEmpty()) {
            etUserFullName.setError(getResources().getString(R.string.error_user_name_required));
            errors = true;
        }
        
        String password = etUserPassword.getText().toString();
        
        if (password.isEmpty()) {
            etUserPassword.setError(getResources().getString(R.string.error_user_password_required));
            errors = true;
        }
        
        if (errors) {
            return null;
        }

        mLoggedUser.setName(name);
        
        if (!PASSWORD_PLACEHOLDER.equals(password)) {
            mLoggedUser.setPassword(password);
        }
        
        return mLoggedUser;
    }

    
}
