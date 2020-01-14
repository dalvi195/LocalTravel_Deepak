package com.deepak.localtravel.Model;

public class Event {

    public String eventId;
    public String image;
    public String description;
    public String name;
    public String lat;
    public String lang;

    public Event(String eventId, String image, String description, String name, String lat, String lang) {
        this.eventId = eventId;
        this.image = image;
        this.description = description;
        this.name = name;
        this.lat = lat;
        this.lang = lang;
    }

    public Event() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
