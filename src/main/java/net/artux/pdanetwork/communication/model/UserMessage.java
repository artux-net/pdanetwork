package net.artux.pdanetwork.communication.model;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.utilities.model.DBMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserMessage {

    public String senderLogin;
    public String message;
    public Date time;
    public int groupId;
    public String avatarId;
    public int pdaId;

    //TODO !!!!!!!!!!!!!!!!
    DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");

    public UserMessage() {
        senderLogin = "System";
        avatarId = "30";
        pdaId = 0;
        groupId = 0;
    }

    public UserMessage(Member member, String message) {
        senderLogin = member.getLogin();
        this.message = message.strip();

        time = new Date();
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

        userMessage.time = new Date();
        return userMessage;
        //TODO: different locales!
    }

    @Override
    public String toString() {

        return "{" +
                "\"senderLogin\":\"" + senderLogin + "\"," +
                "\"message\":\"" + message + "\"," +
                "\"time\":\"" + dateFormat.format(time) + "\"," +
                "\"groupId\":\"" + groupId + "\"," +
                "\"avatarId\":\"" + avatarId + "\"," +
                "\"pdaId\":\"" + pdaId + "\"" +
                '}';
    }
}
