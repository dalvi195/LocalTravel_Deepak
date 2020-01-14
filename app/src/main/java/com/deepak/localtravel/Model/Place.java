package com.deepak.localtravel.Model;



public class Place {
    public String name;
    public String image;
    public String map;
    public String description;
    public String lat;
    public String lng;
    public String favorite;
    public String category;

    public Place() {
    }

    public Place(String name, String image, String map, String description, String lat, String lng, String favorite, String category) {
        this.name = name;
        this.image = image;
        this.map = map;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.favorite = favorite;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}



