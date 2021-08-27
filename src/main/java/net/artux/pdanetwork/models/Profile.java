package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.Getter;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.profile.Achievement;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;

import java.util.List;

@Data
@Getter
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
    private int requests;
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
        this.friends = member.getFriends().size();
        this.requests = member.getFriendRequests().size();
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
        this.friends = member.getFriends().size();
        this.requests = member.getFriendRequests().size();
        this.relations = member.getRelations();

        setFriendStatus(member, by);
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
