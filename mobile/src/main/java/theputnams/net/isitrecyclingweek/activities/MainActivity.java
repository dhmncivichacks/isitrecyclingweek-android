package theputnams.net.isitrecyclingweek.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;


import theputnams.net.isitrecyclingweek.R;
import theputnams.net.isitrecyclingweek.fragments.AboutFragment;
import theputnams.net.isitrecyclingweek.fragments.NavigationDrawerFragment;
import theputnams.net.isitrecyclingweek.util.NavItem;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private CharSequence mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// set the activity
        String x;
    }

    @Override
    public void onNavigationDrawerItemSelected(int selectedIndex) {

        NavigationDrawerFragment navDrawer = ((NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer));
        NavItem navItem = navDrawer.getSelectedNavItem(selectedIndex);
        if (navItem != null) {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AboutFragment(), "NewFragmentTag");
            ft.commit();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
}

