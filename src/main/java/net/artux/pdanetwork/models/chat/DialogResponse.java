package net.artux.pdanetwork.models.chat;

import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.models.communication.ConversationEntity;

public class DialogResponse {

    public String title;
    public int id;
    /*private final int type;
    private final String lastMessage;*/
    private final String avatar;

    public DialogResponse(ConversationEntity conversationEntity, Profile profile) {
        if(profile==null){
            this.title = "Deleted account";
            this.avatar = "0";
        }else {
            this.title = profile.getLogin() + " PDA #" + profile.getPdaId();
            this.avatar = profile.getAvatar();
        }

       /* this.type = 0;
        this.lastMessage = conversation.lastMessage;
        this.id = conversation.cid;*/

    }

    public DialogResponse(ConversationEntity conversationEntity) {
        /*this.title = conversation.title;
        this.type = 0;
        this.lastMessage = conversation.lastMessage;
        this.id = conversation.cid;*/
        this.avatar = "30";
    }

}
