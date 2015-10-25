package theputnams.net.isitrecyclingweek.restclients.service;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import theputnams.net.isitrecyclingweek.restclients.model.CollectionEvent;

/**
 * This is the api that talks to @mikeputnam's scraper
 * http://appletonapi.appspot.com/
 */
public interface CollectionApi {

    @GET("/garbagecollection")
    void getCollectionDates(@Query("addr") String Address, Callback<CollectionEvent[]> cb);
}
