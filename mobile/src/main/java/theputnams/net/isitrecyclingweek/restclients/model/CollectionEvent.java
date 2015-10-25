package theputnams.net.isitrecyclingweek.restclients.model;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class CollectionEvent {

    @Expose
    private Date collectionDate;

    @Expose
    private String collectionType;

    /**
     *
     * @return
     */
    public Date getCollectionDate() {
        return collectionDate;
    }

    /**
     *
     * @param collectionDate
     */
    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    /**
     *
     * @return
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     *
     * @param collectionType
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }
}
