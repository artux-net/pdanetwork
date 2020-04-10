package net.artux.pdanetwork.authentication.register.model;

public class RegisterUser {

    private String login;
    private String password;
    private String email;
    private String name;
    private String avatar;

    public RegisterUser(String login, String name, String email, String password, String avatar) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    public String hashPassword(){
        password = String.valueOf(password.hashCode());
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAvatarId() {
        return avatar;
    }

    public void setAvatarId(String avatarId) {
        this.avatar = avatar;
    }
}

