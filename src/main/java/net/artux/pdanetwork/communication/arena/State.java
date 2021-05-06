package net.artux.pdanetwork.communication.arena;

import java.io.Serializable;
import java.util.HashMap;

public class State implements Serializable {

    public HashMap<String, Entity> entities = new HashMap<>();

}
