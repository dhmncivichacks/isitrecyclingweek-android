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

    public static class GarbageAndOrRecycleFragment extends Fragment {

        public View view = null;
        public ImageView imageView = null;
        public TextView tvAnswerYesNope = null;
        public TextView tvInstructions = null;
        public TextView tvNextPickup = null;
        public TextView tvPropertyKey = null;

        public GarbageAndOrRecycleFragment() {
        }

        /*
        Get users property selection from filesystem
         */
        private JSONArray getStoredPropertyKey() {
            JSONArray propertyKey = null;
            String FILENAME = "isitrecyclingweek.dat";
            Log.d("DEBUG", "Attempting to load persisted data file: " + FILENAME);
            File path = Environment.getExternalStorageDirectory();
            boolean mkdirsSuccess = path.mkdirs();
            if(!mkdirsSuccess){Log.w("WARN","Could not create directories. ");}
            File file = new File(path, FILENAME);

            if (!file.exists() || file.isDirectory()) {
                //First time user, lets go over and set an initial location
                startActivity(new Intent(getActivity(), SearchActivity.class));
            } else {

                //Open the persistence file and grab it's property key
                try {
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

                    byte[] contents = new byte[1024];

                    int bytesRead;
                    String fileContents = "";
                    while ((bytesRead = in.read(contents)) != -1) {
                        fileContents = new String(contents, 0, bytesRead);
                    }
                    Log.d("strFileContents: ", fileContents);
                    propertyKey = new JSONArray(fileContents);
                } catch (IOException e) {
                    Log.e("ERROR", "Failed to open persisted data file. " + e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return propertyKey;
        }

        /*
        Using the property key, go fetch the latest data from the api
         */
        private JSONArray fetchData(JSONArray propertyKey) throws JSONException {

            String http_payload = null;

            try {
                if (propertyKey != null) {
                    http_payload = new WebAsyncTask().execute(getString(R.string.api_uri) + "/property/" + propertyKey.get(0)).get();
                }
                Log.d("fetchData: ", http_payload);
            } catch (ExecutionException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }

            return new JSONArray(http_payload);
        }

        /*
        Using all the property data get the recycling date
         */
        private Date getRecycleDate(JSONArray propertyData) {

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
            tvPropertyKey = (TextView) view.findViewById(R.id.text_property_key);

            return view;

        }

        private void updateView() {
            try {
                JSONArray propertyKey = getStoredPropertyKey();
                if(propertyKey != null) {
                    JSONArray propertyData = fetchData(propertyKey);
                    Date recycleDate = getRecycleDate(propertyData);



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

                    /*
                    Upstream bumps the recycle date forward at 12am on recycle day.
                    This results in misrepresenting the actual day of pickup. :(
                     */
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
                        imageView.setImageResource(R.drawable.recycling_week);
                        tvAnswerYesNope.setText(R.string.yes);
                        tvInstructions.setText(R.string.put_out_the_recycling_bin);
                    } else {
                        //garbage only
                        Log.d("WEEK? ", "garbage only");
                        imageView.setImageResource(R.drawable.garbage_week);
                        tvAnswerYesNope.setText(R.string.nope);
                        tvInstructions.setText(R.string.garbage_bin_only);
                    }

                    //Display property depicted
                    Log.d("propertyKey",propertyKey.get(1).toString() + " " + propertyKey.get(2).toString());
                    tvPropertyKey.setText(propertyKey.get(1).toString() + " " + propertyKey.get(2).toString());

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
