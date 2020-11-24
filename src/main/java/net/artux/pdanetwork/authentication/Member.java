package net.artux.pdanetwork.authentication;

import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.utills.Security;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Member {

    private ObjectId id;
    private String login;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String avatar;
    private String token;
    private int pdaId;
    private int admin;
    private int blocked;
    private int group;
    private int xp;
    private int money;
    private String location;
    private Data data;
    public List<Integer> dialogs;
    public List<Integer> friends;
    public List<Integer> friendRequests;
    public List<Integer> relations;
    private Date lastModified;
    private String registrationDate;
    private Date lastLoginAt;

    public Member() {
    }

    public Member(RegisterUser registerUser, int id) {
        login = registerUser.login;
        password = registerUser.getHashPassword();
        email = registerUser.email;
        name = registerUser.name;
        nickname = registerUser.nickname;
        avatar = registerUser.avatar;
        token = Security.encrypt(login + ":" + password);
        pdaId = id;
        admin = blocked = group = xp = 0;
        location = "Ч-4";
        data = new Data();
        money = 500;
        dialogs = friends = friendRequests = new ArrayList<>();
        lastModified = lastLoginAt = new Date();
        relations = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            relations.add(0);
        registrationDate = new SimpleDateFormat("dd MM yyyy", Locale.US).format(new Date());
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void hashPassword(String password) {
        this.password = String.valueOf(password.hashCode());
        token = Security.encrypt(login + ":" + password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPdaId() {
        return pdaId;
    }

    public void setPdaId(int pdaId) {
        this.pdaId = pdaId;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getBlocked() {
        return blocked;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Integer> getDialogs() {
        return dialogs;
    }

    public void setDialogs(List<Integer> dialogs) {
        this.dialogs = dialogs;
    }

    public List<Integer> getFriends() {
        return friends;
    }

    public void setFriends(List<Integer> friends) {
        this.friends = friends;
    }

    public List<Integer> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<Integer> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Integer> getRelations() {
        return relations;
    }

    public void setRelations(List<Integer> relations) {
        this.relations = relations;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void money(int money) {
        this.money += money;
        if (this.money < 0)
            this.money = 0;
    }

    public void xp(int xp) {
        this.xp += xp;
        if (this.xp < 0)
            this.xp = 0;
    }

    public boolean buy(int price) {
        if (money >= price) {
            money -= price;
            return true;
        } else
            return false;
    }
}
