package net.artux.pdanetwork.authentication.register.model;

public class RegisterUser {

    public String login;
    private String password;
    public String email;
    public String name;
    public String nickname;
    public String avatar;

    public RegisterUser(String login, String name, String email, String password, String avatar) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    public String getHashPassword() {
        password = String.valueOf(password.hashCode());
        return password;
    }

    public String getPassword() {
        return password;
    }
}

