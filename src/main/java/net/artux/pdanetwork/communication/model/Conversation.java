package net.artux.pdanetwork.communication.model;

import java.util.Collections;
import java.util.List;

public class Conversation {

    int id;
    public String title;
    private List<Integer> owners;
    private List<Integer> members;
    String lastMessage;

    public Conversation(int id, List<Integer> owners, List<Integer> members) {
        this.id = id;
        this.owners = owners;
        this.members = members;
        this.members.addAll(owners);
        Collections.sort(members);
    }

    public boolean has(int id){
        return owners.contains(id) || members.contains(id);
    }

    public List<Integer> getMembers() {
        List<Integer> ids = owners;
        ids.addAll(members);
        return ids;
    }
}
