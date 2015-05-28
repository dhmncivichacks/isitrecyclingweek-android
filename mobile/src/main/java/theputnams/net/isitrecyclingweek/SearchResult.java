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

import org.json.JSONArray;

public class SearchResult {

    private String propertyKey;
    private String house_num;
    private String street;

    public SearchResult(String propertyKey, String house_num, String street) {
        super();
        this.propertyKey = propertyKey;
        this.house_num = house_num;
        this.street = street;
    }

    public JSONArray toJSONArray() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(this.propertyKey);
        jsonArray.put(this.house_num);
        jsonArray.put(this.street);
        return jsonArray;
    }

    //This is how android ArrayAdapter does custom lists from custom objects
    @Override
    public String toString() {
        return this.house_num + " " + this.street;
    }

}
