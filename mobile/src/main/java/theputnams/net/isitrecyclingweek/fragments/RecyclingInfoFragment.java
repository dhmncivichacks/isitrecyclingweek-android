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

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import theputnams.net.isitrecyclingweek.R;
import theputnams.net.isitrecyclingweek.restclients.model.CollectionEvent;
import theputnams.net.isitrecyclingweek.restclients.service.ApiService;
import theputnams.net.isitrecyclingweek.util.RecyclingLogicHandler;

public class RecyclingInfoFragment extends Fragment {

    @Bind(R.id.recycling_message)
    TextView mRecyclingMessage;

    @Bind(R.id.recycling_text)
    TextView mRecyclingText;

    @Bind(R.id.recycling_image)
    ImageView mRecyclingImage;

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
        String api_url = prefs.getString(getString(R.string.pref_api_url), null);

        if (address == null || api_url == null) {
            // We're in a bad state... have them update settings
            mRecyclingText.setText(getString(R.string.error_missing_data));
        }

        try {
            String encodedAddress = URLEncoder.encode(address);
            ApiService.getCollectionApi().getCollectionDates(encodedAddress, new Callback<CollectionEvent[]>() {

                @Override
                public void success(CollectionEvent[] collectionEvents, retrofit.client.Response response) {

                    RecyclingLogicHandler logicHandler = new RecyclingLogicHandler(collectionEvents);
                    if (logicHandler.isRecyclingWeek()) {
                        mRecyclingImage.setImageResource(R.drawable.recycling_week_icon);
                        mRecyclingMessage.setText(getString(R.string.yes));
                        String recyclingText = getString(R.string.put_out_the_recycling_bin);
                        recyclingText = String.format(recyclingText, logicHandler.getPickUpDate());
                        mRecyclingText.setText(recyclingText);
                    } else {
                        mRecyclingImage.setImageResource(R.drawable.garbage_week_icon);
                        mRecyclingMessage.setText(getString(R.string.nope));
                        String recyclingText = getString(R.string.garbage_bin_only);
                        recyclingText = String.format(recyclingText, logicHandler.getPickUpDate());
                        mRecyclingText.setText(recyclingText);
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