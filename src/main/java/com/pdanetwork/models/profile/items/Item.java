package com.pdanetwork.models.profile;

public class Item {

    public int id;
    public int type;
    public String icon;
    public String title;
    public float weight;
    public int library_id;
    public int price;


    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public float getWeight() {
        return weight;
    }

    public int getLibrary_id() {
        return library_id;
    }

    public int getPrice() {
        return price;
    }
}
