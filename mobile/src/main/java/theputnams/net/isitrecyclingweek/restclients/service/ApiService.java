package theputnams.net.isitrecyclingweek.restclients.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Getting around the fact that we are dealing with 2 different API's
 */
public class ApiService {

    private static RestAdapter mContractRestAdapter;

    private static RestAdapter mCollectionRestAdapter;

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    public static ContractApi getContractApi() {
        String url = "http://civic-hack-api-locator.azurewebsites.net";

        if (mContractRestAdapter == null) {
            mContractRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(url)
                    .setConverter(new GsonConverter(gson))
                    .build();
        }

        return mContractRestAdapter.create(ContractApi.class);
    }

    public static CollectionApi getCollectionApi() {
        String url = "http://appletonapi.appspot.com";

        if (mCollectionRestAdapter == null) {
            mCollectionRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(url)
                    .setConverter(new GsonConverter(gson))
                    .build();
        }

        return mCollectionRestAdapter.create(CollectionApi.class);
    }
}
