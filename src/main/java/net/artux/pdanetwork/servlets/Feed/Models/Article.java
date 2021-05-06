package net.artux.pdanetwork.servlets.Feed.Models;

import java.time.Instant;
import java.util.List;

public class Article extends FeedModel {

    private String content;

    public Article() {
    }

    public Article(int feedId, String title, String image, List<String> tags, String description, String content) {
        this.feedId = feedId;
        this.title = title;
        this.image = image;
        this.tags = tags;
        this.description = description;
        this.content = content;
        this.published = Instant.now().toEpochMilli();
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPublished() {
        return published;
    }

    public void setPublished(long published) {
        this.published = published;
    }
}
