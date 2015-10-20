/*
 * Copyright (c) 2015 Mike Putnam <mike@theputnams.net>
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

package theputnams.net.isitrecyclingweek;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class GarbageAndOrRecycleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbageandorrecycle);
        if (savedInstanceState == null) {

            SharedPreferences preferences = getSharedPreferences("recyclingWeek", MODE_PRIVATE);
            GarbageAndOrRecycleFragment.Address = preferences.getString("address", null);

            getFragmentManager().beginTransaction()
                    .add(R.id.container, new GarbageAndOrRecycleFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_garbageandorrecycle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setlocation:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setLocation(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    public static class GarbageAndOrRecycleFragment extends Fragment {

        public static String Address;

        public View view = null;
        public ImageView imageView = null;
        public TextView tvAnswerYesNope = null;
        public TextView tvInstructions = null;
        public TextView tvNextPickup = null;
        public TextView tvuserAddress = null;

        public GarbageAndOrRecycleFragment() {
        }

        /*
        Get users property selection from filesystem
         */
        private String getStoredUserAddress() {
            String userAddress = null;

            if (Address == null) {
                //First time user, lets go over and set an initial location
                startActivity(new Intent(getActivity(), SearchActivity.class));
            } else {

                //Fetch the user's stored address
                try {
                    userAddress = new String(Address);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return userAddress;
        }

        /*
        Using the user's address, go fetch the latest data from the api
         */
        private JSONArray fetchData(String userAddress) throws JSONException {

            String http_payload = null;

            try {
                if (userAddress != null) {
                    //FIXME this needs to be derived from
                    http_payload = new WebAsyncTask().execute("http://appletonapi.appspot.com/garbagecollection?addr=" + URLEncoder.encode(userAddress, "UTF-8")).get();
                }
                Log.d("fetchData: ", http_payload);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return new JSONArray(http_payload);
        }

        /*
        Using all the property data get the recycling date
         */
        private Date getRecycleDate(JSONArray propertyData) {
/*
[
    {
        "collectionDate": "2015-10-26",
        "collectionType": "trash"
    },
    {
        "collectionDate": "2015-11-02",
        "collectionType": "trash"
    },
    {
        "collectionDate": "2015-11-02",
        "collectionType": "recycling"
    }
]
*/
            Date recycleDate = null;

            //The chunk of json from the api that contains the recycling date string
            JSONObject jsonRecyclingDate = null;
            try {
                jsonRecyclingDate = propertyData.getJSONObject(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Turn "Monday, 04-06-2015" into "04-06-2015"
            String stringRecycleDate = null;
            try {
                if (jsonRecyclingDate != null) {
                    stringRecycleDate = jsonRecyclingDate.getString("recycleday").split(" ")[1];
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("recycleDate: ", stringRecycleDate);

            SimpleDateFormat month_day_year = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

            try {
                recycleDate = month_day_year.parse(stringRecycleDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return recycleDate;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.primary_linear_layout, container, false);
            imageView = (ImageView) view.findViewById(R.id.imageview_garbageorrecyclingbins);
            tvAnswerYesNope = (TextView) view.findViewById(R.id.text_answer_yes_nope);
            tvInstructions = (TextView) view.findViewById(R.id.text_instructions);
            tvNextPickup = (TextView) view.findViewById(R.id.text_next_pickup);
            tvuserAddress = (TextView) view.findViewById(R.id.text_property_key);

            return view;

        }

        private void updateView() {
            try {
                String userAddress = getStoredUserAddress();
                if(userAddress != null) {
                    JSONArray collectionSchedule = fetchData(userAddress);

/*                    Date recycleDate = getRecycleDate(collectionSchedule);



                    Calendar c = new GregorianCalendar();
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    Date today = c.getTime();

                    Integer days_until_next_recycling_date = ((int) ((recycleDate.getTime() / (24 * 60 * 60 * 1000)) - (int) (today.getTime() / (24 * 60 * 60 * 1000))));
                    Integer days_until_next_pickup;

                    //Garbage weekly, recycling every two weeks.
                    if (days_until_next_recycling_date >= 7) {
                        days_until_next_pickup = days_until_next_recycling_date - 7;
                    } else {
                        days_until_next_pickup = days_until_next_recycling_date;
                    }

                    *//*
                    Upstream bumps the recycle date forward at 12am on recycle day.
                    This results in misrepresenting the actual day of pickup. :(
                     *//*
                    if (days_until_next_recycling_date.equals(Integer.valueOf(14)) || days_until_next_pickup.equals(Integer.valueOf(0))) {
                        tvNextPickup.setText(R.string.pickup_today);
                    } else if (days_until_next_pickup == 1) {
                        tvNextPickup.setText(R.string.pickup_tomorrow);
                    } else {
                        String strNextPickupFormat = getResources().getString(R.string.pickup_in_n_days);
                        String strNextPickup = String.format(
                                Locale.ENGLISH, //TODO handle any language
                                strNextPickupFormat,
                                days_until_next_pickup);
                        tvNextPickup.setText(strNextPickup);
                    }

                    if (days_until_next_recycling_date < 7 || days_until_next_recycling_date.equals(Integer.valueOf(14))) {
                        //recycling week
                        Log.d("WEEK? ", "recycling");
                        imageView.setImageResource(R.drawable.recycling_week_icon);
                        tvAnswerYesNope.setText(R.string.yes);
                        tvInstructions.setText(R.string.put_out_the_recycling_bin);
                    } else {
                        //garbage only
                        Log.d("WEEK? ", "garbage only");
                        imageView.setImageResource(R.drawable.garbage_week_icon);
                        tvAnswerYesNope.setText(R.string.nope);
                        tvInstructions.setText(R.string.garbage_bin_only);
                    }*/

                    //Display property depicted
                    Log.d("userAddress",userAddress + " " + userAddress);
                    tvuserAddress.setText(userAddress);

                } else {
                    startActivity(new Intent(getActivity(), SearchActivity.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            Log.d("DEBUG", "onStart()");
            updateView();
        }

    }
}
