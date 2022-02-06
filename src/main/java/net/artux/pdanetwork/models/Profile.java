package net.artux.pdanetwork.models;

import lombok.Data;

import java.util.List;

@Data
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

    public Profile(UserEntity userEntity) {
        this.login = userEntity.getLogin();
        this.name = userEntity.getName();
        this.role = userEntity.getRole();
        this.blocked = userEntity.getBlocked();
        this.group = userEntity.getGroup();
        this.avatar = userEntity.getAvatar();
        this.pdaId = userEntity.getPdaId();
        this.xp = userEntity.getXp();
        this.location = userEntity.getLocation();
        this.registration = userEntity.getRegistration();
        this.friends = userEntity.getSubs().size();
        this.subs = userEntity.getRequests().size();
        this.relations = userEntity.getRelations();
    }

    public Profile(UserEntity userEntity, UserEntity by) {
        this.login = userEntity.getLogin();
        this.name = userEntity.getName();
        this.role = userEntity.getRole();
        this.blocked = userEntity.getBlocked();
        this.group = userEntity.getGroup();
        this.avatar = userEntity.getAvatar();
        this.pdaId = userEntity.getPdaId();
        this.xp = userEntity.getXp();
        this.location = userEntity.getLocation();
        this.registration = userEntity.getRegistration();
        this.friends = userEntity.getSubs().size();
        this.subs = userEntity.getRequests().size();
        this.relations = userEntity.getRelations();

        friendStatus = getFriendStatus(userEntity, by);
    }

    public static int getFriendStatus(UserEntity userEntity, UserEntity by) {
        if (userEntity.getRequests() != null)
            if (userEntity.getSubs().contains(by.get_id())) {
                return 3;
            } else if (userEntity.getRequests().contains(by.get_id())) {
                return 2;
            } else if (userEntity.getFriends().contains(by.get_id())) {
                return 1;
            } else {
                return  0;
            }
        else return 0;
    }
}
