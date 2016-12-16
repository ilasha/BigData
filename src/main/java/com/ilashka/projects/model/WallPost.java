package com.ilashka.projects.model;


import java.util.ArrayList;
import java.util.Date;

public final class WallPost {
    private int id;
    private String text;
    private Date date;
    private ArrayList<String> hashtags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public WallPost() {
        hashtags = new ArrayList<>();
    }

    public void addHashtag(String text ) {
        hashtags.add(text);
    }
}
