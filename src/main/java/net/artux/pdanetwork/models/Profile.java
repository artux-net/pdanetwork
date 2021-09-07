package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class Profile {

    private String login;
    private String name;
    private String role;
    private int blocked;
    private int group;
    private String avatar;
    private int pdaId;
    private int xp;
    private String location;
    private Long registration;
    private int friendStatus;
    /*
    0 - is not friend
    1 - friend
    2 - subscriber
    3 - requested
     */
    private int friends;
    private int subs;
    private List<Integer> relations;

    private int achievements;

    public Profile() {
    }

    public Profile(Member member) {
        this.login = member.getLogin();
        this.name = member.getName();
        this.role = member.getRole();
        this.blocked = member.getBlocked();
        this.group = member.getGroup();
        this.avatar = member.getAvatar();
        this.pdaId = member.getPdaId();
        this.xp = member.getXp();
        this.location = member.getLocation();
        this.registration = member.getRegistration();
        this.friends = member.getSubs().size();
        this.subs = member.getRequests().size();
        this.relations = member.getRelations();
    }

    public Profile(Member member, Member by) {
        this.login = member.getLogin();
        this.name = member.getName();
        this.role = member.getRole();
        this.blocked = member.getBlocked();
        this.group = member.getGroup();
        this.avatar = member.getAvatar();
        this.pdaId = member.getPdaId();
        this.xp = member.getXp();
        this.location = member.getLocation();
        this.registration = member.getRegistration();
        this.friends = member.getSubs().size();
        this.subs = member.getRequests().size();
        this.relations = member.getRelations();

        friendStatus = getFriendStatus(member, by);
    }

    public static int getFriendStatus(Member member, Member by) {
        if (member.getRequests() != null)
            if (member.getSubs().contains(by.get_id())) {
                return 3;
            } else if (member.getRequests().contains(by.get_id())) {
                return 2;
            } else if (member.getFriends().contains(by.get_id())) {
                return 1;
            } else {
                return  0;
            }
        else return 0;
    }
}
