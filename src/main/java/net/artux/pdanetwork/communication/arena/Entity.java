package net.artux.pdanetwork.communication.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Profile;

import java.io.Serializable;

public class Entity implements Serializable {


    final static double MOVEMENT = 5;

    public final long id;
    private final String name;
    public double health = 100;
    public final Vector2 position;
    public Vector2 velocity;
    public Vector2 size;

    public Entity(Vector2 position, Profile profile) {
        this.position = position;
        velocity = new Vector2(0,0);
        this.id = profile.getPdaId();
        name = profile.getLogin();
        size = new Vector2(32, 32);
    }

    public Entity(Vector2 position, Vector2 direction) {
        this.position = position;
        velocity = direction;
        this.id = -1;
        name = "bullet";
    }

    public void act(){
        if(velocity.x!=0 || velocity.y != 0){
            this.position.moveBy( velocity.x * MOVEMENT,  velocity.y * MOVEMENT);
        }

    }

    public Pair<Vector2, Vector2> getCollider(){
        return new Pair<>(position, new Vector2(position.x + size.x, position.y + size.y));
    }

    @RequiredArgsConstructor
    @Getter
    static class Pair<T,Y>{

        private final T right;
        private final Y left;

    }
}
