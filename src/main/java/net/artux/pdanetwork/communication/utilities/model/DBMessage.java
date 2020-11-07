package net.artux.pdanetwork.communication.utilities.model;

import net.artux.pdanetwork.communication.model.UserMessage;

import java.util.Date;

public class DBMessage {

    public String message;
    public Date time;
    public int pdaId;

    public DBMessage() {
    }

    public DBMessage(UserMessage userMessage) {
        pdaId = userMessage.pdaId;
        time = userMessage.time;
        message = userMessage.message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPdaId() {
        return pdaId;
    }

    public void setPdaId(int pdaId) {
        this.pdaId = pdaId;
    }
}
