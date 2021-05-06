package net.artux.pdanetwork.communication.arena;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.Profile;

import java.io.Serializable;

public class Entity implements Serializable {


    final static double MOVEMENT = 0.4;

    private final Vector2 position;
    private Vector2 velocity;
    private final Profile profile;

    public Entity(Vector2 position, Member profile) {
        this.position = position;
        this.profile = new Profile(profile);
    }

    public void move(Vector2 position) {
        this.position.moveBy(position.x * MOVEMENT, position.y * MOVEMENT);
    }
}
