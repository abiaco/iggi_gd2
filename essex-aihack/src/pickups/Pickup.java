package pickups;

import asteroids.GameObject;
import battle.SimpleBattle;
import math.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static pickups.Constants.*;

public class Pickup extends GameObject {
    // Visuals properties
    static int nVerts = 5;
    static int vertsStep = 2;
    int radius = 12;
    static Stroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    int[] px, py;
    double rotation;
    double rotationRate;

    // Object specificc properties
    public int containedScore = 100;
    SimpleBattle game;

    public Pickup(SimpleBattle game, Vector2d s) {
        super(s, new Vector2d(0, 0));
        this.game = game;
        rotation = 0;
        rotationRate = 30;

        double pointsScaling = 1 + 2 *rand.nextDouble();
        radius *= pointsScaling;
        containedScore *= pointsScaling;

        setPolygon();
    }

    public boolean dead() {
        return dead;
    }

    private void setPolygon() {
        px = new int[nVerts];
        py = new int[nVerts];

        for (int i=0 ; i<nVerts ; i++) {
            double theta = (Math.PI * 2 / nVerts) * (i * vertsStep);
            px[i] = (int) (radius * Math.cos(theta));
            py[i] = (int) (radius * Math.sin(theta));
        }
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        if (isTarget) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.magenta);
        }
        g.translate(s.x, s.y);
        g.rotate(rotation);
        g.setColor(Color.yellow);
        g.setStroke(stroke);
        g.fillPolygon(px, py, px.length);
        g.setTransform(at);
    }

    @Override
    public GameObject copy() {
        Pickup pickup = new Pickup(game, s);
        updateClone(pickup);
        return pickup;
    }

    public void update() {
        rotation += rotationRate;
    }

    public String toString() {
        return s.toString();
    }

    public void hit(SimpleBattle bollocks) {
        dead = true;
    }
}
