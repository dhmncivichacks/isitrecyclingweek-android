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
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SearchFragment())
                    .commit();
        }
    }

    public void button_search_onclick(View view) {

        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                return Character.isLetterOrDigit(c) || Character.isSpaceChar(c);
            }
        };

        EditText input_house_number = (EditText)findViewById(R.id.house_number);
        input_house_number.setFilters(new InputFilter[] { inputFilter });
        String string_house_number = input_house_number.getText().toString();

        EditText input_street_name = (EditText)findViewById(R.id.street_name);
        input_street_name.setFilters(new InputFilter[] { inputFilter });
        String string_street_name = input_street_name.getText().toString();

        String http_payload = null;
        try {
            http_payload = new WebAsyncTask().execute(getString(R.string.api_uri) + "/search?h="+string_house_number+"&s="+string_street_name).get();
            if (http_payload != null) {
                Log.d("http_payload: ", http_payload);

                JSONArray json_array = new JSONArray(http_payload);
                Log.d("json_array: ", String.valueOf(json_array));

                int length = json_array.length();
                ArrayList<String> result_arraylist = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    result_arraylist.add(json_array.getString(i));
                }

                if (result_arraylist.size() < 1) {
                    Toast.makeText(getApplicationContext(),"Sorry. Nothing found.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(this, SearchResultListActivity.class);
                    intent.putStringArrayListExtra("result_arraylist",result_arraylist);
                    startActivity(intent);
                }

            } else {
                Toast.makeText(getApplicationContext(),"Sorry. Cannot include cardinal directions or street types. Base street name only.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static class SearchFragment extends Fragment {

        public SearchFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //View
            return inflater.inflate(R.layout.fragment_search, container, false);
        }
    }
}
