package net.artux.pdanetwork.communication.model;

import java.util.ArrayList;

public class LimitedArrayList<Object> extends ArrayList<Object> {

    int limitMessages;

    public LimitedArrayList(int limitMessages) {
        this.limitMessages = limitMessages;
    }

    @Override
    public boolean add(Object e) {
        if (this.size() < limitMessages) {
            return super.add(e);
        } else {
            this.remove(0);
            return super.add(e);
        }
    }
}