package net.artux.pdanetwork.models;

import net.artux.pdanetwork.authentication.Member;

public class UserInfo {

    private String login;
    private int pdaId;
    private int group;
    private String avatar;
    private String location;
    private int xp;
    private String regDate;

    public UserInfo(Member member) {
        login = member.getLogin();
        pdaId = member.getPdaId();
        group = member.getGroup();
        avatar = member.getAvatar();
        location = member.getLocation();
        xp = member.getXp();
        regDate = member.getRegistrationDate();
    }

    public String getLogin() {
        return login;
    }

    public int getPdaId() {
        return pdaId;
    }

    public int getGroup() {
        return group;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLocation() {
        return location;
    }

    public int getXp() {
        return xp;
    }

    public String getRegDate() {
        return regDate;
    }
}
