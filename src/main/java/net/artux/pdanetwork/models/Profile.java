package net.artux.pdanetwork.models;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.profile.Achievement;

import java.util.List;

public class Profile {

    private String login;
    private String name;
    private int admin;
    private int blocked;
    private int group;
    private String avatar;
    private int pdaId;
    private int xp;
    private String location;
    private String registrationDate;
    private int friendStatus;
    /*
    0 - is not friend
    1 - friend
    2 - subscriber
    3 - requested
     */
    private int friends;
    private int requests;
    private List<Integer> relations;

    //TODO
    private List<Achievement> achievements;

    public Profile() {
    }

    public Profile(Member member) {
        this.login = member.getLogin();
        this.name = member.getName();
        this.admin = member.getAdmin();
        this.blocked = member.getBlocked();
        this.group = member.getGroup();
        this.avatar = member.getAvatar();
        this.pdaId = member.getPdaId();
        this.xp = member.getXp();
        this.location = member.getLocation();
        this.registrationDate = member.getRegistrationDate();
        this.friends = member.getFriends().size();
        this.requests = member.getFriendRequests().size();
        this.relations = member.getRelations();
    }

    public Profile(Member member, Member by) {
        this.login = member.getLogin();
        this.name = member.getName();
        this.admin = member.getAdmin();
        this.blocked = member.getBlocked();
        this.group = member.getGroup();
        this.avatar = member.getAvatar();
        this.pdaId = member.getPdaId();
        this.xp = member.getXp();
        this.location = member.getLocation();
        this.registrationDate = member.getRegistrationDate();
        this.friends = member.getFriends().size();
        this.requests = member.getFriendRequests().size();
        this.relations = member.getRelations();

        setFriendStatus(member, by);
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public int getAdmin() {
        return admin;
    }

    public int getBlocked() {
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

    private void setFriendStatus(Member member, Member by) {
        if (member.getFriendRequests() != null)
            if (member.getFriendRequests().contains(by.getPdaId())) {
                friendStatus = 3;
            } else if (by.getFriendRequests().contains(pdaId)) {
                friendStatus = 2;
            } else if (member.getFriends().contains(by.getPdaId())) {
                friendStatus = 1;
            } else {
                friendStatus = 0;
            }
        else friendStatus = 0;
    }
}
