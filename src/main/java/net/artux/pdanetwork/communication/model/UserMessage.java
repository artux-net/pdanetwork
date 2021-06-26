package net.artux.pdanetwork.communication.model;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.utilities.model.DBMessage;

import java.time.Instant;

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

    public UserMessage(int cid, Member member, String message) {
        this.cid = cid;
        senderLogin = member.getLogin();
        this.message = message.strip();

        time = Instant.now().toEpochMilli();
        groupId = member.getGroup();
        avatarId = member.getAvatar();
        pdaId = member.getPdaId();
    }

    public UserMessage(Member member, String message) {
        senderLogin = member.getLogin();
        this.message = message.strip();

        time = Instant.now().toEpochMilli();
        groupId = member.getGroup();
        avatarId = member.getAvatar();
        pdaId = member.getPdaId();
    }

    public UserMessage(Member member, DBMessage message) {
        senderLogin = member.getLogin();
        this.message = message.message;

        time = message.time;
        groupId = member.getGroup();
        avatarId = member.getAvatar();
        pdaId = member.getPdaId();
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
