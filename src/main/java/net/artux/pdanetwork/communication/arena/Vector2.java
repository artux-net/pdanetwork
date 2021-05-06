package net.artux.pdanetwork.communication.arena;

import java.io.Serializable;

public class Vector2 implements Serializable {

    public double x;
    public double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void moveBy(double x, double y) {
        this.x += x;
        this.y += y;
    }
}
