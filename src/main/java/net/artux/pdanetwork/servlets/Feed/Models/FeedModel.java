package net.artux.pdanetwork.servlets.Feed.Models;

import java.util.ArrayList;
import java.util.List;

public class FeedModel {

    public int feedId;
    public String title;
    public String image;
    public List<String> tags = new ArrayList<>();
    public String description;
    public long published;

}
