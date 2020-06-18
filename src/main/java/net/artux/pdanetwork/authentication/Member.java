package net.artux.pdanetwork.authentication;

import com.google.gson.Gson;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.profile.Data;

import java.util.List;

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
    private List<Integer> dialogs;
    private String lastModified;
    private String registrationDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

        return new Profile(login, name, admin, blocked,
                group, avatar, pdaId, xp, location, registrationDate, data);
    }

    public int getPdaId() {
        return pdaId;
    }

    public void setPdaId(int pdaId) {
        this.pdaId = pdaId;
    }

    public int getGroup() {
        return group;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Integer> getDialogs() {
        return dialogs;
    }

    Data getData(Gson gson) {
        if(data==null || data.equals("") || data.equals("null") || data.equals("{}")){
            data = gson.toJson(new Data());
        }

        return gson.fromJson(data, Data.class);
    }
}
