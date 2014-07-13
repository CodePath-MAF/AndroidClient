
package org.missionassetfund.apps.android;

import org.missionassetfund.apps.android.fragments.ProfileFragment;
import org.missionassetfund.apps.android.models.User;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

public class EditProfileActivity extends FragmentActivity {

    private ProfileFragment mProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mProfileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragmentProfile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);

        return true;
    }

    public void onSaveProfile(MenuItem menuItem) {
        User user = mProfileFragment.getModifiedUser();
        
        if (user == null) {
            return;
        }

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException exception) {
                if (exception != null) {
                    Log.d(EditProfileActivity.class.getName(), "error saving user", exception);

                    if (exception.getCode() == ParseException.PASSWORD_MISSING) {
                        Toast.makeText(EditProfileActivity.this,
                                R.string.parse_error_password_missing,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // All other errors return a generic message
                        Toast.makeText(EditProfileActivity.this, R.string.parse_error_saving,
                                Toast.LENGTH_SHORT).show();
                    }

                    return;
                }

                Toast.makeText(EditProfileActivity.this, R.string.parse_success_user_save,
                        Toast.LENGTH_SHORT).show();
                EditProfileActivity.this.finish();
            }
        });
    }
}
