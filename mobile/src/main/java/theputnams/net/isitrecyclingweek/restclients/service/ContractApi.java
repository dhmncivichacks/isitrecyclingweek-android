package theputnams.net.isitrecyclingweek.restclients.service;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import theputnams.net.isitrecyclingweek.restclients.model.APIContract;

/**
 * This is the rest api that pings the civichacks site
 * http://civic-hack-api-locator.azurewebsites.net/#/swagger
 */
public interface ContractApi {

    @GET("/api/implementations/byzipcode/{zipCode}")
    void getContractsByZip(@Path("zipCode") String zipCode, Callback<APIContract[]> cb);

}
