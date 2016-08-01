/*
 * Copyright (c) 2015 Mike Putnam <mike@theputnams.net>
 * Copyright (c) 2015 Jake Kiser <jacobvkiser@gmail.com>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

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
    private ArrayList<NavItem> mNavItems;
    private LayoutInflater mInflater;

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
        mNavItems.add(new INavItem(NavLocation.SETTINGS));
        mNavItems.add(new INavItem(NavLocation.RECYCLING_INFO));

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
        // Should be view holder pattern but there aren't many items so it won't hurt us
        NavItem nav = mNavItems.get(position);
        view = mInflater.inflate(R.layout.nav_item_clickable, null);
        ((TextView) view.findViewById(R.id.tv_nav_text))
                .setText(nav.getLocation().name());
        return view;
    }
}
