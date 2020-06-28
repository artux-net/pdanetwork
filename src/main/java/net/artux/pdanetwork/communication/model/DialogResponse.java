package net.artux.pdanetwork.communication.model;

import net.artux.pdanetwork.models.Profile;

public class DialogResponse {

    public String title;
    public int id;
    private int type;
    private String lastMessage;
    private String avatar;

    public DialogResponse(Conversation conversation, Profile profile) {
        this.title = profile.getLogin() + " PDA #" + profile.getPdaId();
        this.type = 0;
        this.lastMessage = conversation.lastMessage;
        this.id = conversation.id;
        this.avatar = profile.getAvatar();
    }

    public DialogResponse(Conversation conversation){
        this.title = conversation.title;
        this.type = 0;
        this.lastMessage = conversation.lastMessage;
        this.id = conversation.id;
        this.avatar = "30";
    }

}
