package asteroids;

import battle.SimpleBattle;
import math.Vector2d;

import java.awt.*;

public abstract class GameObject {
    public Vector2d s,v;
    public boolean isTarget;
    public boolean dead;
    public double r;

    protected GameObject(Vector2d s, Vector2d v) {
        this.s = new Vector2d(s, true);
        this.v = new Vector2d(v, true);
    }

    public abstract void update();
    public abstract void draw(Graphics2D g);
    public abstract GameObject copy();

    protected GameObject updateClone(GameObject copyObject) {
        copyObject.s = new Vector2d(s, true);
        copyObject.v = new Vector2d(v, true);
        copyObject.isTarget = isTarget;
        copyObject.dead = dead;
        copyObject.r = r;

        return copyObject;
    }

    public abstract boolean dead();

    public void hit(SimpleBattle hitBy) {
        dead  = true;
    }

    public double r() {
        return r;
    }

    public boolean wrappable() {
        // wrap objects by default
        return true;
    }
}
