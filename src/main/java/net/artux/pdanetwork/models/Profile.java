package net.artux.pdanetwork.models;

import java.util.List;

public class Profile {

    private String login;
    private String name;
    private byte admin;
    private byte blocked;
    private int group;
    private String avatar;
    private int pdaId;
    private int xp;
    private String location;
    private String registrationDate;
    private String data;
    private List<Integer> friends;
    private List<Integer> requests;

    public Profile(String login, String name, byte admin, byte blocked, int group, String avatar, int pdaId, int xp,
                   String location, String registrationDate, String data, List<Integer> friends, List<Integer> requests) {
        this.login = login;
        this.name = name;
        this.admin = admin;
        this.blocked = blocked;
        this.group = group;
        this.avatar = avatar;
        this.pdaId = pdaId;
        this.xp = xp;
        this.location = location;
        this.registrationDate = registrationDate;
        this.data = data;
        this.friends = friends;
        this.requests = requests;
    }

    public List<Integer> getFriends() {
        return friends;
    }

    public List<Integer> getRequests() {
        return requests;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public byte getAdmin() {
        return admin;
    }

    public byte getBlocked() {
        return blocked;
    }

    public int getGroup() {
        return group;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getPdaId() {
        return pdaId;
    }

    public int getXp() {
        return xp;
    }

    public String getLocation() {
        return location;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getData() {
        return data;
    }
}
