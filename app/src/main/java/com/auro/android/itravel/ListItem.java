package com.auro.android.itravel;

public class ListItem {
    private String catImage;
    private String placeName;
    private String placeAddr;
    private String placeID;
    private int fav;

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setPlaceAddr(String placeAddr) {
        this.placeAddr = placeAddr;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public ListItem(String catImage, String placeName, String placeAddr, String placeID, int fav) {
        this.catImage = catImage;
        this.placeName = placeName;
        this.placeAddr = placeAddr;
        this.placeID = placeID;
        this.fav = fav;
    }

    public String getCatImage() {
        return catImage;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceAddr() {
        return placeAddr;
    }

    public String getPlaceID() {
        return placeID;
    }

    public int getFav() {
        return fav;
    }
}
