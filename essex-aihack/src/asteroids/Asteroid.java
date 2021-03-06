package asteroids;

import battle.NeuroShip;
import battle.SimpleBattle;
import math.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Arrays;


import static asteroids.Constants.*;

public class Asteroid extends GameObject {
    static int nPoints = 16;
    static double radialRange = 0.6;
    static Stroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    int[] px, py;
    double rotRate;
    double rot;
    boolean dead;
    int index;
    //GameState game;

    public Asteroid(Vector2d s, Vector2d v, int index) {
        super(s, v);
        //this.game = game;
        rotRate = (rand.nextDouble() - 0.5) * Math.PI / 20;
        rot = 0;
        this.index = index;
        r = radii[2-index];
        setPolygon();
    }

    public boolean dead() {
        return dead;
    }

    public void setPolygon() {
        px = new int[nPoints];
        py = new int[nPoints];
        for (int i = 0; i < nPoints; i++) {
            // generate within certain ranges
            //  in polar coords (radians)
            // then transform to cartesian
            double theta = (Math.PI * 2 / nPoints)
                    * (i + rand.nextDouble());
            double rad = r * (1 - radialRange / 2
                    + rand.nextDouble() * radialRange);
            px[i] = (int) (rad * Math.cos(theta));
            py[i] = (int) (rad * Math.sin(theta));
        }
//        System.out.println(Arrays.toString(px));
//        System.out.println(Arrays.toString(py));
    }

    public void draw(Graphics2D g) {
        // store coordinate system
        AffineTransform at = g.getTransform();
        if (isTarget) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.magenta);
        }
        g.translate(s.x, s.y);
        // System.out.println("Drawing at " + s);
        g.rotate(rot);
        // g.fillPolygon(px, py, px.length);
        g.setColor(Color.white);
        g.setStroke(stroke);
        g.drawPolygon(px, py, px.length);
        // restore original coordinate system
        g.setTransform(at);
    }

    @Override
    public GameObject copy() {
        Asteroid asteroid = new Asteroid(s, v, index);
        updateClone(asteroid);
        return asteroid;
    }

    public void update() {
        s.add(v);
        rot += rotRate;
    }
    public String toString() {
        return s.toString();
    }

    public void hit(SimpleBattle hitBy) {
        dead = true;
        //game.asteroidDeath(this);

            // if we still have smaller ones to
            // work through then do so
            // otherwise do nothing
            // score += asteroidScore;
            if (index > 0) {
                // add some new ones at this position
                for (int i=0; i<SimpleBattle.nSplits; i++) {

                    Vector2d v1 = new Vector2d(v,true);
                    v1.add(rand.nextGaussian(), rand.nextGaussian());
                    Asteroid a1 = new Asteroid(new Vector2d(s, true), v1, index - 1);
                    hitBy.objects.add(a1);
                }
            }
    }
}
