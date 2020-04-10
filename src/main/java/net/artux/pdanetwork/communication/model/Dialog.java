package net.artux.pdanetwork.communication.model;

public class Dialog {

    public String name;
    private int toPdaId;
    private String pdaAvatar;
    private String pdaLogin;
    private String lastMessage;

    public Dialog(String name, int toPdaId, String pdaLogin, String pdaAvatar, String lastMessage) {
        this.name = name;
        this.toPdaId = toPdaId;
        this.pdaLogin = pdaLogin;
        this.pdaAvatar = pdaAvatar;
        this.lastMessage = lastMessage;
    }
}
