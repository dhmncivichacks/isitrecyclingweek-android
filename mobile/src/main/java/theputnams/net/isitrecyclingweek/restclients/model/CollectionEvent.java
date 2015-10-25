package theputnams.net.isitrecyclingweek.restclients.model;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class CollectionEvent {

    @Expose
    private Date collectionDate;
    @Expose
    private String collectionType;

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }
}
