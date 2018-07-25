package com.auro.android.itravel;

public class ReviewItem {
    private String aname;
    private String aphoto;
    private String arating;
    private String atext;
    private String atime;
    private String rid;


    public ReviewItem(String aname, String aphoto, String arating, String atext, String atime, String rid) {
        this.aname = aname;
        this.aphoto = aphoto;
        this.arating = arating;
        this.atext = atext;
        this.atime = atime;
        this.rid = rid;
    }


    public String getRid() {
        return rid;
    }


    public String getAname() {
        return aname;
    }

    public String getAphoto() {
        return aphoto;
    }

    public String getArating() {
        return arating;
    }

    public String getAtext() {
        return atext;
    }

    public String getAtime() {
        return atime;
    }
}
