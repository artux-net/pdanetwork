package net.artux.pdanetwork.models.communication;

import java.util.LinkedList;

public class LimitedLinkedList<Object> extends LinkedList<Object> {

    int limitMessages;

    public LimitedLinkedList(int limitMessages) {
        this.limitMessages = limitMessages;
    }

    @Override
    public boolean add(Object e) {
        if (this.size() >= limitMessages) {
            this.removeFirst();
        }
        return super.add(e);
    }
}