package theputnams.net.isitrecyclingweek.restclients.model;

import com.google.gson.annotations.Expose;

public class APIContract {

    @Expose
    private String contractName;
    @Expose
    private String contractDescription;
    @Expose
    private String implementationName;
    @Expose
    private String homepageUrl;
    @Expose
    private String implementationApiUrl;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractDescription() {
        return contractDescription;
    }

    public void setContractDescription(String contractDescription) {
        this.contractDescription = contractDescription;
    }

    public String getImplementationName() {
        return implementationName;
    }

    public void setImplementationName(String implementationName) {
        this.implementationName = implementationName;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    public String getImplementationApiUrl() {
        return implementationApiUrl;
    }

    public void setImplementationApiUrl(String implementationApiUrl) {
        this.implementationApiUrl = implementationApiUrl;
    }
}
