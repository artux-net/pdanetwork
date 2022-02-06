package net.artux.pdanetwork.communication.model;

import lombok.Data;
import net.artux.pdanetwork.models.UserEntity;
import net.artux.pdanetwork.communication.utilities.model.DBMessage;

import java.time.Instant;

@Data
public class UserMessage {

    public int cid;
    public String senderLogin;
    public String message;
    public long time;
    public int groupId;
    public String avatarId;
    public int pdaId;

    public UserMessage() {
        senderLogin = "System";
        avatarId = "30";
        pdaId = 0;
        groupId = 0;
    }

    public UserMessage(int cid, UserEntity userEntity, String message) {
        this.cid = cid;
        senderLogin = userEntity.getLogin();
        this.message = message.trim();

        time = Instant.now().toEpochMilli();
        groupId = userEntity.getGroup();
        avatarId = userEntity.getAvatar();
        pdaId = userEntity.getPdaId();
    }

    public UserMessage(UserEntity userEntity, String message) {
        senderLogin = userEntity.getLogin();
        this.message = message.trim();

        time = Instant.now().toEpochMilli();
        groupId = userEntity.getGroup();
        avatarId = userEntity.getAvatar();
        pdaId = userEntity.getPdaId();
    }

    public UserMessage(UserEntity userEntity, DBMessage message) {
        senderLogin = userEntity.getLogin();
        this.message = message.message;

        time = message.time;
        groupId = userEntity.getGroup();
        avatarId = userEntity.getAvatar();
        pdaId = userEntity.getPdaId();
    }

    public static UserMessage getSystemMessage(String loc, String message){
        UserMessage userMessage = new UserMessage();
        userMessage.message = message;

        userMessage.time = Instant.now().toEpochMilli();
        return userMessage;
        //TODO: different locales!
    }

    @Override
    public String toString() {

        return "{" +
                "\"senderLogin\":\"" + senderLogin + "\"," +
                "\"message\":\"" + message + "\"," +
                "\"time\":\"" + time + "\"," +
                "\"groupId\":\"" + groupId + "\"," +
                "\"avatarId\":\"" + avatarId + "\"," +
                "\"pdaId\":\"" + pdaId + "\"" +
                '}';
    }
}
