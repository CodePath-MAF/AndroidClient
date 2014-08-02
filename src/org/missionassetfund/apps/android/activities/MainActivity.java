
package org.missionassetfund.apps.android.activities;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.fragments.DashboardFragment;
import org.missionassetfund.apps.android.fragments.DashboardFragment.SwitchMainFragmentListener;
import org.missionassetfund.apps.android.fragments.GoalsListFragment;
import org.missionassetfund.apps.android.fragments.InitialSetupFragment;
import org.missionassetfund.apps.android.fragments.InitialSetupFragment.OnInitialSetupListener;
import org.missionassetfund.apps.android.models.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.parse.ParseException;

public class MainActivity extends BaseFragmentActivity implements SwitchMainFragmentListener,
        OnInitialSetupListener {
    public static final int NEW_GOAL_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateFragments();
    }

    private void updateFragments() {
        User currentUser = (User) User.getCurrentUser();

        // Try to get a fresh copy of the user
        try {
            currentUser.refresh();
        } catch (ParseException e) {
            Log.d("Error", "Could not get fresh user");
            Log.d("Error", e.getMessage());
        }

        if (currentUser.isSetup()) {
            showDashboardFragment();
        } else {
            showInitialSetupFragment();
        }
    }

    private void showDashboardFragment() {
        showFragment(DashboardFragment.class);
    }

    private void showInitialSetupFragment() {
        showFragment(InitialSetupFragment.class);
    }

    @SuppressWarnings("rawtypes")
    private void showFragment(Class activeFragmentClass) {
        Class[] fragmentClasses = new Class[] {
                DashboardFragment.class,
                InitialSetupFragment.class
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
                        transaction.add(R.id.frmMainContent, (Fragment) klass.newInstance(),
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
    public void SwitchToFragment(Class<? extends Fragment> klass) {
        showFragment(klass);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_profile:
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == NEW_GOAL_REQUEST_CODE) {
            GoalsListFragment fragmentGoalList = (GoalsListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.goalListFragment);
            fragmentGoalList.updateGoalList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccessfulSetup() {
        hideSoftKeyboard();
        setProgressBarIndeterminateVisibility(false);
        updateFragments();
        Toast.makeText(this, getString(R.string.initial_setup_toast_successful_message),
                Toast.LENGTH_SHORT).show();
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
}
