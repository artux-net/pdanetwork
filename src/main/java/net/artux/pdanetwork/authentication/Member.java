package net.artux.pdanetwork.authentication;

import com.google.gson.Gson;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.profile.Data;

public class Member {

    private String login;
    private String email;
    private String name;
    private String avatar;
    private String token;
    private int pdaId;
    private byte admin;
    private byte blocked;
    private int group;
    private int xp;
    private String location;
    private String data;
    private String dialogs;
    private String lastModified;
    private String registrationDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Profile getProfile(){
        Profile profile = new Profile(login, name, admin, blocked,
                group, avatar, pdaId, xp, location, registrationDate, data);

        return profile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getPdaId() {
        return pdaId;
    }

    public void setPdaId(int pdaId) {
        this.pdaId = pdaId;
    }

    public byte getAdmin() {
        return admin;
    }

    public void setAdmin(byte admin) {
        this.admin = admin;
    }

    public byte getBlocked() {
        return blocked;
    }

    public void setBlocked(byte blocked) {
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

    public void setData(String data) {
        this.data = data;
    }

    public String getDialogs() {
        return dialogs;
    }

    public void setDialogs(String dialogs) {
        this.dialogs = dialogs;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public Data getData() {
        Gson gson = new Gson();
        if(data==null || data.equals("") || data.equals("null") || data.equals("{}")){
            data = gson.toJson(new Data());
        }

        return gson.fromJson(data, Data.class);
    }

    public void updateData(Data data) {
        this.data = new Gson().toJson(data);
    }
}
