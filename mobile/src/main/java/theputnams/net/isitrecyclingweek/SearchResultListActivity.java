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

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SearchResultListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        //Receive the full JSON payload from the SearchActivity
        ArrayList<String> searchResult = getIntent().getStringArrayListExtra("result_arraylist");

        //Instantiate our data object with overridden toString() for selective list fields
        ArrayList<SearchResult> searchResultList = new ArrayList<>();
        for(int i = 0; i < searchResult.size(); i++) {
            try {
                JSONArray singleResult = new JSONArray(searchResult.get(i));
                searchResultList.add(new SearchResult(singleResult.get(0).toString(), singleResult.get(1).toString(), singleResult.get(2).toString()));
            } catch (NullPointerException | JSONException e) {
                e.printStackTrace();
            }
        }

        //Use our custom data object for the result list (excludes ugly JSON bits from the list)
        ArrayAdapter<SearchResult> searchResultArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResultList);
        setListAdapter(searchResultArrayAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Cast the users selection to our custom data object and use it's toJSONArray method to persist JSON to a file
        SearchResult searchResult = (SearchResult) l.getItemAtPosition(position);
        String selection = searchResult.toJSONArray().toString();
        Log.d("LIST CLICK",selection);

        //Try saving the user's choice to a file for subsequent runs of the app
        try {
            SharedPreferences preferences = getSharedPreferences("recyclingWeek", MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();

            edit.putString("address", selection);
            edit.commit();

            //With choice saved on the filesystem, let's go render the main activity!
            startActivity(new Intent(this, GarbageAndOrRecycleActivity.class));

        } catch (Exception e) {
            Log.e("ERROR", "Failed to write file output. " + e);
        }

    }

}
