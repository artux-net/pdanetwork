package net.artux.pdanetwork.models.profile.items;

import java.util.HashMap;

public class Item {

    public int id;
    public int type;
    public String icon;
    public String title;
    public float weight;
    public int library_id;
    public int price;
    public HashMap<String, String> data;

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
