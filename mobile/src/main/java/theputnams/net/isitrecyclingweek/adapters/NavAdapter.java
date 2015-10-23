package theputnams.net.isitrecyclingweek.adapters;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import theputnams.net.isitrecyclingweek.R;
import theputnams.net.isitrecyclingweek.util.INavItem;
import theputnams.net.isitrecyclingweek.util.NavItem;
import theputnams.net.isitrecyclingweek.util.NavLocation;

public class NavAdapter extends BaseAdapter
{
    private ArrayList<NavItem> mNavItems;                              //List of NavItems that can be selected/their data
    private LayoutInflater mInflater;                               //LayoutInflater for inflating individual items

    public NavAdapter(Fragment fragment)
    {
        mNavItems = new ArrayList<NavItem>();
        mInflater = fragment.getActivity()
                .getLayoutInflater();
        this.initialize();
    }

    public void initialize()
    {
        mNavItems.clear();

        mNavItems.add(new INavItem(NavLocation.ABOUT));
        mNavItems.add(new INavItem(NavLocation.SEARCH));

        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        // Should be View Holder pattern but there aren't enough items for it to matter.
        NavItem nav = mNavItems.get(position);
        view = mInflater.inflate(R.layout.nav_item_clickable, null);
        ((TextView) view.findViewById(R.id.tv_nav_text))
                .setText(nav.getLocation().name());
        return view;
    }
}
