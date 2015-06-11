package Game;

import utils.Vector2D;

import java.awt.*;

public class Bullet extends GameObject {

    public static final int RADIUS = 2;
    public static final double lifetime = 1000;
    public int current_life;
    Ship ship;

    Bullet(Ship ship) {
        Vector2D sv = new Vector2D(ship.position);
        sv.add(ship.d);
        position = new Vector2D(sv);
        velocity = new Vector2D(ship.velocity);
        dead = false;
    }

    Bullet() {
        position = new Vector2D();
        velocity = new Vector2D();
        dead = false;
    }

    Bullet(Vector2D s, Vector2D v) {
        this.position = s;
        this.velocity = v;
        dead = false;
        current_life = 0;
    }

    @Override
    public double radius() {
        return RADIUS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void update() {
        this.current_life += 5;
        if (this.current_life > lifetime) {
            this.dead = true;
        } else {
            position.add(velocity, Constants.DT);
            position.wrap(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.green);
        g.fillOval((int) position.x - RADIUS / 2, (int) position.y - RADIUS / 2, 2 * RADIUS, 2 * RADIUS);
    }
}
