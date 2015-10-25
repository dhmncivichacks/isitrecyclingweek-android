package theputnams.net.isitrecyclingweek.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;


import theputnams.net.isitrecyclingweek.R;
import theputnams.net.isitrecyclingweek.fragments.AboutFragment;
import theputnams.net.isitrecyclingweek.fragments.NavigationDrawerFragment;
import theputnams.net.isitrecyclingweek.fragments.RecyclingInfoFragment;
import theputnams.net.isitrecyclingweek.fragments.SettingsFragment;
import theputnams.net.isitrecyclingweek.util.NavItem;
import theputnams.net.isitrecyclingweek.util.NavLocation;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private CharSequence mTitle;

    protected NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// set the activity
        this.mNavigationDrawerFragment = ((NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer));
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
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

        setTitle(navItem.getLocation().name());

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, navItem.getLocation().name())
                .addToBackStack(null)
                .commit();
    }

    public Fragment getFragmentByNavLocation(NavLocation location) {

        switch(location) {
            case ABOUT:
                return new AboutFragment();
            case SETTINGS:
                return new SettingsFragment();
            case RECYCLING_INFO:
                return new RecyclingInfoFragment();
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

