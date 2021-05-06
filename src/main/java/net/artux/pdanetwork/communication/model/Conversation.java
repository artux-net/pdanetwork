package net.artux.pdanetwork.communication.model;

import java.util.List;

public class Conversation {

    public int cid;
    public String title;
    private List<Integer> owners;
    private List<Integer> members;
    String lastMessage;

    public Conversation() {
    }

    public Conversation(int cId, List<Integer> owners, List<Integer> members) {
        this.cid = cId;
        this.owners = owners;
        this.members = members;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getOwners() {
        return owners;
    }

    public void setOwners(List<Integer> owners) {
        this.owners = owners;
    }

    public List<Integer> getMembers() {
        return members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean has(int id) {
        return owners.contains(id) || members.contains(id);
    }

    public List<Integer> allMembers() {
        List<Integer> ids = owners;
        ids.addAll(members);
        return ids;
    }


    @Override
    public String toString() {
        return "{\"cid\" : " + cid + ",\"title\" : " + (title == null ? null : "\"" + title + "\"") + ",\"owners\" : " + (owners == null ? null : owners) + ",\"members\" : " + (members == null ? null : members) + ",\"lastMessage\" : " + (lastMessage == null ? null : lastMessage) + "}";
    }
}
