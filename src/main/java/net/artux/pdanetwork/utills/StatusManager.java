package net.artux.pdanetwork.utills;

import net.artux.pdanetwork.models.Status;

import java.util.LinkedHashMap;
import java.util.Map;

public class StatusManager {

    private Map<String, Status> statuses = new LinkedHashMap<>();

    StatusManager(){

    }

    public Status getStatus(String loc, String id) {
        return statuses.get(loc+"_"+id);
    }
}
