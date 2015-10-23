package theputnams.net.isitrecyclingweek.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;


import theputnams.net.isitrecyclingweek.R;
import theputnams.net.isitrecyclingweek.fragments.AboutFragment;
import theputnams.net.isitrecyclingweek.fragments.NavigationDrawerFragment;
import theputnams.net.isitrecyclingweek.util.NavItem;
import theputnams.net.isitrecyclingweek.util.NavLocation;

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
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(navItem.getLocation().name());

        if (fragment == null) {
            fragment = getFragmentByNavLocation(navItem.getLocation());
        }

        final FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, new AboutFragment(), navItem.getLocation().name());
        ft.commit();
    }

    public Fragment getFragmentByNavLocation(NavLocation location) {

        switch(location) {
            case ABOUT:
                return new AboutFragment();
            case SEARCH:
                return new AboutFragment();
            default:
                throw new IllegalArgumentException("Invalid location");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
}

