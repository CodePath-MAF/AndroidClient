
package org.missionassetfund.apps.android.activities;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.fragments.DashboardFragment;
import org.missionassetfund.apps.android.fragments.DashboardFragment.SwitchMainFragmentListener;
import org.missionassetfund.apps.android.fragments.NewGoalFragment;
import org.missionassetfund.apps.android.models.User;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements SwitchMainFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showDashboardFragment();
    }

    private void showDashboardFragment() {
        showFragment(DashboardFragment.class);
    }

    @SuppressWarnings("rawtypes")
    private void showFragment(Class activeFragmentClass) {
        Class[] fragmentClasses = new Class[] {
                DashboardFragment.class
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

            case R.id.action_logout:
                logoutParse();
                break;
            case R.id.action_add_goal:
                FragmentManager fm = getSupportFragmentManager();
                NewGoalFragment newGoalFragment = new NewGoalFragment();
                newGoalFragment.show(fm, "fragment_new_goal");
                break;
            case R.id.action_edit_profile:
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    private void logoutParse() {
        User.logOut();

        // FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
        // logs out on older devices, we'll just exit.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Intent intent = new Intent(this, MAFDispatchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            finish();
        }
    }
}
