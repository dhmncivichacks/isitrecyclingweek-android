/*
 * Copyright (c) 2015 Mike Putnam <mike@theputnams.net>
 * Copyright (c) 2015 Jake Kiser <jacobvkiser@gmail.com>
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
            return new SimpleDateFormat("MM/dd/yyyy").format(this.getGarbageEvent().getCollectionDate());
        } else return "";
    }
}
