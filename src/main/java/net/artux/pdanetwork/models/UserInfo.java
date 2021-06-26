package net.artux.pdanetwork.models;

import net.artux.pdanetwork.authentication.Member;

public class UserInfo {

    private final String login;
    private final int pdaId;
    private final int group;
    private final String avatar;
    private final String location;
    private final int xp;
    private final Long regDate;

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

    public Long getRegDate() {
        return regDate;
    }

    @Override
    public String toString() {
        return "{\"login\" : " + (login == null ? null : "\"" + login + "\"") + ",\"pdaId\" : " + pdaId + ",\"group\" : " + group + ",\"avatar\" : " + (avatar == null ? null : "\"" + avatar + "\"") + ",\"location\" : " + (location == null ? null : "\"" + location + "\"") + ",\"xp\" : " + xp + ",\"regDate\" : " + (regDate == null ? null : "\"" + regDate + "\"") + "}";
    }
}
