package net.artux.pdanetwork.communication.arena;

import net.artux.pdanetwork.models.Profile;

import javax.websocket.Session;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

public class State implements Serializable {

    public int frame = 0;
    public HashMap<String, Entity> players = new HashMap<>();
    public ArrayList<Entity> entities = new ArrayList<>();
    public ArrayList<Entity> shoots = new ArrayList<>();

    public void act(){
        frame++;
        ListIterator<Entity> iterator = entities.listIterator();
        while (iterator.hasNext()){
            Entity e = iterator.next();
            e.act();
            if (e.id < 0)
                iterator.remove();
        }
    }

    public void dispose(){
        shoots.clear();
    }

    public void addPlayer(Session session, Profile profile){
        players.put(session.getId(), new Entity(new Vector2(0,0), profile));
    }

    public void removePlayer(Session session){
        players.remove(session.getId());
    }
}
