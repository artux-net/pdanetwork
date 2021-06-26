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

    public boolean isRayHit(Vector2 line, Vector2 point1, Vector2 point2){
        double a = (line.y - y) / (line.x - x);
        double b = line.y - a * line.x;

        int counter = 0;
        double y = a * point1.x + b;

        if(y > point1.y)
            counter++;

        if(y > point2.y)
            counter++;

        y = a * point2.x + b;

        if(y > point1.y)
            counter++;

        if(y > point2.y)
            counter++;

        return counter > 0 && counter < 4;
    }
}
