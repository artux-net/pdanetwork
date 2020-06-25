package net.artux.pdanetwork.models;

public class FriendModel {

    int id;
    int group;
    String name;
    public boolean isSub = false;

    public FriendModel(Profile profile) {
        this.id = profile.getPdaId();
        this.group = profile.getGroup();
        this.name = profile.getLogin();
    }
}
