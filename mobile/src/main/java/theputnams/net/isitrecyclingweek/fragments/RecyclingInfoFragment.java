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

package theputnams.net.isitrecyclingweek.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import theputnams.net.isitrecyclingweek.R;
import theputnams.net.isitrecyclingweek.restclients.model.CollectionEvent;
import theputnams.net.isitrecyclingweek.restclients.service.ApiService;
import theputnams.net.isitrecyclingweek.util.RecyclingLogicHandler;

public class RecyclingInfoFragment extends Fragment {

    @Bind(R.id.recycling_text)
    TextView mRecyclingText;

    @Bind(R.id.recycling_time_frame)
    TextView mRecyclingTimeFrame;

    @Bind(R.id.recycling_image)
    ImageView mRecyclingImage;

    @Bind(R.id.recycling_address)
    TextView mRecyclingAddress;

    @Bind(R.id.recycling_asof)
    TextView mRecyclingAsof;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycling_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateViewData();
    }

    protected void updateViewData() {
        SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.pref_settings), Context.MODE_PRIVATE);
        String address = prefs.getString(getString(R.string.pref_address), null);
        String api_base_url = prefs.getString(getString(R.string.pref_api_base_url), null);
        String api_path = prefs.getString(getString(R.string.pref_api_path), null);

        if (address == null || api_base_url == null || api_path == null) {
            // We're in a bad state... have them update settings
            mRecyclingText.setText(getString(R.string.error_missing_data));
        }

        try {
            String encodedAddress = URLEncoder.encode(address,"UTF-8");
            ApiService.getCollectionApi(api_base_url).getCollectionDates(api_path + "?addr=" + encodedAddress, new Callback<CollectionEvent[]>() {

                @Override
                public void success(CollectionEvent[] collectionEvents, retrofit.client.Response response) {

                    if (collectionEvents.length > 0 ) {
                        RecyclingLogicHandler logicHandler = new RecyclingLogicHandler(collectionEvents);
                        if (logicHandler.isRecyclingWeek()) {
                            mRecyclingImage.setImageResource(R.drawable.recycling_week_icon);
                            mRecyclingText.setText(getString(R.string.put_out_the_recycling_bin));
                        } else {
                            mRecyclingImage.setImageResource(R.drawable.garbage_week_icon);
                            mRecyclingText.setText(getString(R.string.garbage_bin_only));
                        }
                        String recyclingTimeFrame = String.format(getString(R.string.pickup_in_n_days), logicHandler.getPickUpDays());
                        mRecyclingTimeFrame.setText(recyclingTimeFrame);
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd h:mm a").format(Calendar.getInstance().getTime());
                        SharedPreferences inner_prefs = getActivity().getSharedPreferences(getString(R.string.pref_settings), Context.MODE_PRIVATE);
                        mRecyclingAddress.setText(inner_prefs.getString(getString(R.string.pref_address), null));
                        mRecyclingAsof.setText(String.format("As of %1$s", timeStamp));
                    } else {
                        mRecyclingText.setText(getText(R.string.cannot_find_address));
                    }
               }

                @Override
                public void failure(RetrofitError error) {
                    //ToDo set up some sort of error logging for the app
                    Toast.makeText(getActivity(), R.string.error_internal_server_error, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}