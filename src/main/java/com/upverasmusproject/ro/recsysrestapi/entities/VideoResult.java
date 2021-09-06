package com.upverasmusproject.ro.recsysrestapi.entities;

public class VideoResult {
    int id;
    double match;
    String title;

    public VideoResult(int id, double match, String title) {
        this.id = id;
        this.match = match;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMatch() {
        return match;
    }

    public void setMatch(double match) {
        this.match = match;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}