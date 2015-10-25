package theputnams.net.isitrecyclingweek.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import theputnams.net.isitrecyclingweek.restclients.model.CollectionEvent;

/**
 * I created this to deal with the api returning an array of events
 */
public class RecyclingLogicHandler {

    protected CollectionEvent garbageEvent;

    protected CollectionEvent recyclingEvent;

    public RecyclingLogicHandler(CollectionEvent[] collectionEvents) {
        init(collectionEvents);
    }

    public CollectionEvent getGarbageEvent() {
        return garbageEvent;
    }

    public CollectionEvent getRecyclingEvent() {
        return recyclingEvent;
    }

    public void setRecyclingEvent(CollectionEvent recyclingEvent) {
        this.recyclingEvent = recyclingEvent;
    }

    public void setGarbageEvent(CollectionEvent garbageEvent) {
        this.garbageEvent = garbageEvent;
    }

    /**
     * This is a mess but there is no getting around it because of api limitations
     * @param collectionEvents
     * @return
     */
    protected void init(CollectionEvent[] collectionEvents){
        ArrayList<CollectionEvent> sortedEvents = new ArrayList<CollectionEvent>(Arrays.asList(collectionEvents));
        Collections.sort(sortedEvents, new Comparator<CollectionEvent>() {
            @Override
            public int compare(CollectionEvent lhs, CollectionEvent rhs) {
                int dateComp = lhs.getCollectionDate().compareTo(rhs.getCollectionDate());
                if (dateComp != 0) return dateComp;
                return rhs.getCollectionType().compareToIgnoreCase(lhs.getCollectionType());
            }
        });

        this.garbageEvent = sortedEvents.get(0);
        CollectionEvent recycling = sortedEvents.get(1);
        if (recycling != null &&
                this.garbageEvent != null &&
                recycling.getCollectionDate().compareTo(garbageEvent.getCollectionDate()) == 0) {
            this.recyclingEvent = recycling;
        }
    }

    public boolean isRecyclingWeek() {
        return recyclingEvent != null;
    }

    public String getPickUpDate() {
        if (this.getGarbageEvent() != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(this.getGarbageEvent().getCollectionDate());
        } else return "";
    }
}
