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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import theputnams.net.isitrecyclingweek.R;
import theputnams.net.isitrecyclingweek.activities.MainActivity;
import theputnams.net.isitrecyclingweek.restclients.model.APIContract;
import theputnams.net.isitrecyclingweek.restclients.service.ApiService;

/**
 * View used for setting the user address / determining if the api is available for their area
 * Relies on https://github.com/ragunathjawahar/android-saripaar for validation
 */
public class SettingsFragment extends Fragment implements Validator.ValidationListener {

    @Bind(R.id.zip_code)
    @NotEmpty
    EditText mZipCode;

    @Bind(R.id.save)
    Button mSaveButton;

    @Bind(R.id.street_address)
    @NotEmpty
    EditText mStreetAddress;

    @Bind(R.id.city_state)
    @NotEmpty
    EditText mCityState;

    Validator mValidator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View searchView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, searchView);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValidator.validate();
            }
        });
        return searchView;
    }

    @Override
    public void onValidationSucceeded() {
        ApiService.getContractApi().getContractsByZip(mZipCode.getText().toString(), new Callback<APIContract[]>() {
            @Override
            public void success(APIContract[] apiContracts, retrofit.client.Response response) {

                if (apiContracts.length > 0) {
                    StringBuilder address = new StringBuilder(mStreetAddress.getText().toString());
                    address.append(" " + mCityState.getText().toString());
                    address.append(" " + mZipCode.getText().toString());

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.pref_settings), Context.MODE_PRIVATE).edit();

                    // ToDo Replace this with an ORM
                    // Save the address in prefs
                    editor.putString(getString(R.string.pref_address), address.toString());
                    // Save the api url in prefs
                    editor.putString(getString(R.string.pref_api_base_url), apiContracts[0].getImplementationBaseUrl());
                    editor.putString(getString(R.string.pref_api_path), apiContracts[0].getImplementationPath());
                    editor.commit();

                    Toast.makeText(getActivity(), R.string.address_updated, Toast.LENGTH_LONG).show();

                    //Just send them over to the recycling info fragment
                    ((MainActivity) SettingsFragment.this.getActivity()).onNavigationDrawerItemSelected(2);

                } else {
                    Toast.makeText(getActivity(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                //ToDo set up some sort of error logging for the app
                Toast.makeText(getActivity(), R.string.error_internal_server_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();

            if (view instanceof EditText) {
                ((EditText) view).setError(getString(R.string.error_required_field));
            }
        }
    }
}
