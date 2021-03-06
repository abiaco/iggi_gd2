package battle;

import asteroids.*;
import math.Vector2d;
import pickups.Pickup;
import utilities.JEasyFrame;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.*;


import static asteroids.Constants.*;

/**
 * Created by simon lucas on 10/06/15.
 * <p>
 * Aim here is to have a simple battle class
 * that enables ships to fish with each other
 * <p>
 * Might start off with just two ships, each with their own types of missile.
 */

public class SimpleBattle {

    // play a time limited game with a strict missile budget for
    // each player
    static int nMissiles = 100;
    static int nTicks = 1000;
    static int pointsPerKill = 100;
    static int pointsPerAsteroidHit = 20;
    static int releaseVelocity = 5;

    static int logFrequency = 10;

    boolean visible = true;

    public static int nAsteroids = 9;
    public static int nPickups = 3;
    public static int nSplits = 3;

    ArrayList<BattleController> controllers;

    public ArrayList<GameObject> objects;
    ArrayList<PlayerStats> stats;

    NeuroShip s1, s2;
    BattleController p1, p2;
    BattleView view;
    int currentTick;
    BattleLogger logger1, logger2;

    public JEasyFrame windowFrame;

    boolean isLoggingEnabled;

    int gameType;

    public SimpleBattle() {
        this(true, false, 0);
    }

    public SimpleBattle(boolean visible, boolean loggingEnabled, int gameType) {
        this.objects = new ArrayList<>();
        this.stats = new ArrayList<>();
        this.visible = visible;
        this.isLoggingEnabled = loggingEnabled;

        if (visible) {
            view = new BattleView(this);
            windowFrame = new JEasyFrame(view, "battle");
        }

        this.gameType = gameType;
        if(gameType == 0)
        {
            nAsteroids = 9;
            nPickups = 3;
            nSplits = 3;
        }

        if(gameType == 1)
        {
            nAsteroids = 9;
            nPickups = 3;
            nSplits = 0;
        }

        if(gameType == 2)
        {
            nAsteroids = 0;
            nPickups = 6;
            nSplits = 0;
        }
    }

    public int getTicks() {
        return currentTick;
    }

    public int playGame(BattleController p1, BattleController p2) {
        if (isLoggingEnabled) {
            logger1 = new BattleLogger(p1.getClass().toString(), 0);
            logger2 = new BattleLogger(p2.getClass().toString(), 1);
        }

        this.p1 = p1;
        this.p2 = p2;
        reset();

        if (p1 instanceof KeyListener) {
            view.addKeyListener((KeyListener)p1);
            view.setFocusable(true);
            view.requestFocus();
        }

        if (p2 instanceof KeyListener) {
            view.addKeyListener((KeyListener)p2);
            view.setFocusable(true);
            view.requestFocus();
        }

        int framesUntilLog = 1;
        while (!isGameOver()) {
            update();
            if (--framesUntilLog == 0) {
                if(isLoggingEnabled) {
                    logger1.log(this);
                    logger2.log(this);
                }
                framesUntilLog = logFrequency;
            }
        }

        if(isLoggingEnabled) {
            logger1.close();
            logger2.close();
        }

        if (p1 instanceof KeyListener) {
            view.removeKeyListener((KeyListener)p1);
        }
        if (p2 instanceof KeyListener) {
            view.removeKeyListener((KeyListener)p2);
        }

        return 0;
    }

    public void reset() {
        stats.clear();
        objects.clear();
        s1 = buildShip(100, 250, 0);
        s2 = buildShip(500, 250, 1);
        this.currentTick = 0;

        for (int i=0 ; i<nAsteroids ; i++) {
            objects.add(new Asteroid(new Vector2d(width * rand.nextDouble(), height * rand.nextDouble()),
                                     new Vector2d(2 * rand.nextDouble() - 1, 2 * rand.nextDouble() - 1),
                                     2));
        }

        for (int i=0 ; i<nPickups ; i++) {
            objects.add(new Pickup(this,
                                   new Vector2d(width * rand.nextDouble(), height * rand.nextDouble())));
        }

        stats.add(new PlayerStats(0, 0));
        stats.add(new PlayerStats(0, 0));
    }

    protected NeuroShip buildShip(int x, int y, int playerID) {
        Vector2d position = new Vector2d(x, y, true);
        Vector2d speed = new Vector2d(true);
        Vector2d direction = new Vector2d(1, 0, true);

        return new NeuroShip(position, speed, direction, playerID );
    }

    public void update() {
        // get the actions from each player

        // apply them to each player's ship, taking actions as necessary
        Action a1 = p1.getAction(this.clone(), 0);
        //System.out.println(a1.toString());
        Action a2 = p2.getAction(this.clone(), 1);
        update(a1, a2);
    }

    public void update(Action a1, Action a2) {
        // now apply them to the ships
        s1.update(a1);
        s2.update(a2);

        if (a1.shoot) fireMissile(s1.s, s1.d, 0);
        if (a2.shoot) fireMissile(s2.s, s2.d, 1);

        checkCollision(s1);
        checkCollision(s2);
        checkMissileCollisions();

        // and fire any missiles as necessary


        wrap(s1);
        wrap(s2);

        // here need to add the game objects ...
        java.util.List<GameObject> killList = new ArrayList<GameObject>();
        for (GameObject object : objects) {
            object.update();
            wrap(object);
            if (object.dead()) {
                killList.add(object);
            }
        }

        objects.removeAll(killList);
        currentTick++;

        if (visible) {
            view.repaint();
            sleep();
        }
    }


    public SimpleBattle clone() {
        SimpleBattle state = new SimpleBattle(false, true, gameType);
        state.objects = copyObjects();
        state.stats = copyStats();
        state.currentTick = currentTick;
        state.visible = false; //stop MCTS people having all the games :p

        state.s1 = s1.copy();
        state.s2 = s2.copy();
        return state;
    }

    protected ArrayList<GameObject> copyObjects() {
        ArrayList<GameObject> objectClone = new ArrayList<GameObject>();
        for (GameObject object : objects) {
            objectClone.add(object.copy());
        }

        return objectClone;
    }

    protected ArrayList<PlayerStats> copyStats() {
        ArrayList<PlayerStats> statsClone = new ArrayList<PlayerStats>();
        for (PlayerStats object : stats) {
            statsClone.add(new PlayerStats(object.nMissiles, object.nPoints));
        }

        return statsClone;
    }

    protected void checkCollision(GameObject actor) {
        // check with all other game objects
        // but use a hack to only consider interesting interactions
        // e.g. asteroids do not collide with themselves
        if (!actor.dead()) {
            if (actor instanceof NeuroShip) {
                for (GameObject target : objects) {
                    if (overlap(actor, target)) {
                        if(target instanceof Missile) {
                            int playerID = (actor == s1 ? 1 : 0);
                            PlayerStats stats = this.stats.get(playerID);
                            stats.nPoints += pointsPerKill;
                        } else if(target instanceof Asteroid) {
                            int playerID = (actor == s1 ? 0 : 1);
                            PlayerStats stats = this.stats.get(playerID);
                            stats.nPoints -= pointsPerAsteroidHit;
                        } else if (target instanceof Pickup) {
                            int playerID = (actor == s1 ? 0 : 1);
                            PlayerStats stats = this.stats.get(playerID);
                            stats.nPoints += ((Pickup)target).containedScore;
                        }
                        target.hit(this);
                        return;
                    }
                }
            } else if (actor instanceof Missile) {
                for (GameObject target : objects) {
                    if (overlap(actor, target)) {
                        if (target instanceof Asteroid) {
                            //target.hit(this);
                            actor.hit(this);
                            target.hit(this);
                        }
                        return;
                    }
                }
            }
        }
    }

    private void checkMissileCollisions() {
        // Collisions with ships handled elsewhere
        for(GameObject m : copyObjects())
        {
            checkCollision(m);
        }
    }

    private boolean overlap(GameObject actor, GameObject ob) {
        if (actor.equals(ob)) {
            return false;
        }
        // otherwise do the default check
        double dist = actor.s.dist(ob.s);
        boolean ret = dist < (actor.r() + ob.r());
        return ret;
    }

    public void sleep() {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fireMissile(Vector2d s, Vector2d d, int playerId) {
        // need all the usual missile firing code here
        NeuroShip currentShip = playerId == 0 ? s1 : s2;
        PlayerStats stats = this.stats.get(playerId);
        if (stats.nMissiles < nMissiles) {
            Missile m = new Missile(s, new Vector2d(0, 0, true));
            m.v.add(d, releaseVelocity);
            // make it clear the ship
            m.s.add(m.v, (currentShip.r() + missileRadius) * 1.5 / m.v.mag());
            objects.add(m);
            // System.out.println("Fired: " + m);
            // sounds.fire();
            stats.nMissiles++;
        }
    }

    public void draw(Graphics2D g) {
        // for (Object ob : objects)
        if (s1 == null || s2 == null) {
            return;
        }

        // System.out.println("In draw(): " + n);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(bg);
        g.fillRect(0, 0, size.width, size.height);

        for (GameObject go : getObjects()) {
            go.draw(g);
        }

        s1.draw(g);
        if (p1 instanceof RenderableBattleController) {
            RenderableBattleController rbc = (RenderableBattleController)p1;
            rbc.render(g, s1.copy());
        }

        s2.draw(g);
        if (p2 instanceof RenderableBattleController) {
            RenderableBattleController rbc = (RenderableBattleController)p2;
            rbc.render(g, s2.copy());
        }
    }

    public NeuroShip getShip(int playerID) {
        assert playerID < 2;
        assert playerID >= 0;

        if (playerID == 0) {
            return s1.copy();
        } else {
            return s2.copy();
        }
    }

    public ArrayList<GameObject> getObjects()
    {
        return new ArrayList<>(objects);
    }

    public int getPoints(int playerID) {
        assert playerID < 2;
        assert playerID >= 0;

        return stats.get(playerID).nPoints;
    }

    public int getMissilesLeft(int playerID) {
        assert playerID < 2;
        assert playerID >= 0;

        return stats.get(playerID).nMissiles - nMissiles;
    }

    private void wrap(GameObject ob) {
        // only wrap objects which are wrappable
        if (ob.wrappable()) {
            ob.s.x = (ob.s.x + width) % width;
            ob.s.y = (ob.s.y + height) % height;
        }
    }

    public boolean isGameOver() {
        if (getMissilesLeft(0) >= 0 && getMissilesLeft(1) >= 0) {
            //ensure that there are no bullets left in play
            if (objects.isEmpty()) {
                return true;
            }
        }

        return currentTick >= nTicks;
    }

    static class PlayerStats {
        int nMissiles;
        int nPoints;

        public PlayerStats(int nMissiles, int nPoints) {
            this.nMissiles = nMissiles;
            this.nPoints = nPoints;
        }

        public int getMissilesFired() {
            return nMissiles;
        }

        public int getPoints() {
            return nPoints;
        }

        public String toString() {
            return nMissiles + " : " + nPoints;
        }
    }

}
