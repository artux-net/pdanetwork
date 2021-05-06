package net.artux.pdanetwork.communication;

public class Action {

    private final String action;
    private final String data;

    public Action(String action, String data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public String getData() {
        return data;
    }
}

