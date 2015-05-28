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

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class WebAsyncTask extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... urls) {
        try {
            URL url= new URL(urls[0]);

            StringBuilder sb = new StringBuilder();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream input_stream = new BufferedInputStream(conn.getInputStream());
            BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(input_stream));

            String line;
            while ((line = buffered_reader.readLine()) != null) {
                sb.append(line);
            }

            conn.disconnect();

            return sb.toString();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(String result) {}

}