package com.auro.android.itravel;

public class FavItem {
    private String favImage;
    private String favName;
    private String favAddr;
    private String favID;
    private int favIcon;

    public FavItem(String favImage, String favName, String favAddr, String favID, int favIcon) {
        this.favImage = favImage;
        this.favName = favName;
        this.favAddr = favAddr;
        this.favID = favID;
        this.favIcon = favIcon;
    }

    public String getFavImage() {
        return favImage;
    }

    public String getFavName() {
        return favName;
    }

    public String getFavAddr() {
        return favAddr;
    }

    public String getFavID() {
        return favID;
    }

    public int getFavIcon() {
        return favIcon;
    }
}
