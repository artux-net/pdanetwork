package net.artux.pdanetwork.communication.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UserMessage {

    public String senderLogin;
    public String message;
    public String time;
    public int groupId;
    public int avatarId;
    public int pdaId;

    public static UserMessage getSystemMessage(String loc, String message){
        UserMessage userMessage = new UserMessage();
        userMessage.message = message;
        userMessage.senderLogin = "Система";
        userMessage.avatarId = 30;
        userMessage.pdaId = 0;
        userMessage.groupId = 0;

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));

        userMessage.time = dateFormatGmt.format(new Date());
        return userMessage;
        //TODO: different locales!
    }

}
