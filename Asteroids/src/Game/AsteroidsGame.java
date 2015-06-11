package Game;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static Game.Constants.*;

import utils.*;

import javax.swing.*;

public class AsteroidsGame implements Game, Serializable {
    // public boolean MenuSelected;
    private int score;
    private int lives;
    int level;
    boolean over;
    // private static final long serialVersionUID = 7526471155622776147L;

    Random r = new Random();

    public ArrayList<GameObject> objects;
    ArrayList<GameObject> alive;
    ArrayList<GameObject> pending;
    ArrayList<MenuItem> menu;
    ArrayList<MenuItem> shop;
    Ship ship;
    EnemyShip ship2;



    Action action;
    boolean sound;
    public int credits;

    public AsteroidsGame() {
        // MenuSelected = true;
        menu = new ArrayList<MenuItem>();
        shop = new ArrayList<MenuItem>();
        populateMenu();
        populateShop();
        credits = 0;
        this.sound = false;
        this.level = 1;
        this.over = false;
        this.score = 0;
        this.lives = 3;
        this.objects = new ArrayList<GameObject>();
        this.alive = new ArrayList<GameObject>();
        this.pending = new ArrayList<GameObject>();
        ship = new Ship(this, new BasicKeys());
        action = ship.ctrl.action(this);
        action.execute = false;
        action.exit = false;
        action.menu = true;

        action.buy_bomb = false;
        action.buy_life = false;
        action.buy_weapon = false;
        pending.add(ship);

        for (int i = 0; i < N_INITIAL_ASTEROIDS; i++) {
            Asteroid as = new Asteroid(this);
            if (overlap(as, ship)) {
                as.position.x += 45;
            }
            pending.add(as);
        }
        pending.add(new EnemyShip(this, new AimNShoot()));
        pending.add(new EnemyShip(this, new AimNShoot()));
        for (int i = 0; i < N_INITIAL_ASTEROIDS - 3; i++) {
            pending.add(new EnemyShip(this, new SeekNShoot()));
        }

        //}
        Portal s = new Portal(this);
        Portal son = new Portal(this, s);
        s.twin = son;
        pending.add(s);
        pending.add(son);

    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            AsteroidsGame asteroidsGame = new AsteroidsGame();
            View view = new View(asteroidsGame);
            new JEasyFrameFull(view).addKeyListener((BasicKeys) asteroidsGame.ship.ctrl);
            while (!asteroidsGame.over) {
                asteroidsGame.update();
                view.repaint();
                Thread.sleep(DELAY);
            }
            if (asteroidsGame.over) {
                asteroidsGame = new AsteroidsGame();
                view = new View(asteroidsGame);
            }
        }

    }

    public void update() {
        if (action.save) {
            save();
            action.save = false;
            action.menu = false;
        }
        if (action.load) {
            load();
            action.load = false;
            action.menu = false;
        }
        if (!action.menu && !action.shop) {
            for (GameObject object : objects) {
                object.update();
                if (object instanceof Ship || object instanceof Asteroid || object instanceof EnemyShip || object instanceof VaderMine)
                    checkCollision(object);
                if (!object.dead)
                    alive.add(object);
            }
            if (alive.size() == 3) {
                level++;
                for (int i = 0; i < N_INITIAL_ASTEROIDS + level; i++) {
                    Asteroid as = new Asteroid(this);
                    if (overlap(as, ship)) {
                        as.position.x += 45;

                    }
                    pending.add(as);
                }
                for (int i = 0; i < N_INITIAL_ASTEROIDS - 3 + level; i++) {
                    pending.add(new EnemyShip(this, new SeekNShoot()));
                }
                pending.add(new EnemyShip(this, new AimNShoot()));
                pending.add(new EnemyShip(this, new AimNShoot()));
            }
            synchronized (this) {
                objects.clear();
                objects.addAll(pending);
                objects.addAll(alive);
            }
            pending.clear();
            alive.clear();
            if (r.nextInt(10000) == 5) objects.add(new CreditPack(this));
        }

        if (action.buy_life) {
            if (credits > shop.get(1).val) {
                credits -= shop.get(1).val;
                lives++;

            }
            action.buy_life = false;
        }
        if (action.buy_bomb) {
            if (credits > shop.get(2).val) {
                credits -= shop.get(2).val;
                this.ship.bombs++;

            }
            action.buy_bomb = false;
        }
        if (action.buy_weapon) {
            if (this.ship.bullet_level < 3) {
                if (credits > shop.get(0).val) {
                    credits -= shop.get(0).val;
                    this.ship.bullet_level++;

                }
            }
            action.buy_weapon = false;
        }


    }

    @Override
    public Iterable<GameObject> getGameObjects() {
        return objects;
    }

    @Override
    public Iterable<GameObject> getBackgroundObjects() {

        return null;
    }

    @Override


    public void add(GameObject object) {
        pending.add(object);
    }

    public Vector2D getShipLocation() {
        return this.ship.position;
    }

    public void checkCollision(GameObject object) {
        // check with other game objects
        if (!object.dead) {
            for (GameObject otherObject : objects) {
                if (object.getClass() != otherObject.getClass()
                        && overlap(object, otherObject)) {
                    if (object instanceof Ship && otherObject instanceof PowerUp) {
                        otherObject.dead = true;
                        if (((Ship) object).shield.shieldLevel <= ((Ship) object).shield.maxShield)
                            ((Ship) object).shield.shieldLevel++;
                        continue;
                    }
                    if (object instanceof Ship && otherObject instanceof CreditPack) {
                        otherObject.dead = true;
                        credits += 500;
                        continue;
                    }

                    if (object instanceof Ship && otherObject instanceof Asteroid) {
                        ((Ship) object).hit(25);
                        otherObject.hit();
                        continue;
                    }
                    if (object instanceof Ship && otherObject instanceof VaderMine) {
                        continue;
                    }
                    if (object instanceof VaderMine && otherObject instanceof Ship) {
                        continue;
                    }
                    if (!(object instanceof Ship) && otherObject instanceof VaderMine) {
                        object.hit();
                        otherObject.hit();
                        continue;

                    }
                    if (object instanceof Ship && otherObject instanceof Bullet) {
                        ((Ship) object).hit(10);
                        otherObject.hit();
                        continue;
                    }
                    if (object instanceof EnemyShip && otherObject instanceof Bullet) {
                        ((EnemyShip) object).hit(10);
                        otherObject.hit();
                        continue;
                    }
                    if (object instanceof VaderMine && otherObject instanceof Bullet) {
                        object.hit();
                        otherObject.hit();
                        continue;
                    }
                    if (object instanceof EnemyShip && otherObject instanceof Asteroid) {
                        ((EnemyShip) object).hit(25);
                        otherObject.hit();
                        continue;
                    }
                    if (object instanceof Ship && otherObject instanceof Portal) {
                        if (((Portal) otherObject).twinOne) {
                            object.position.x += (((Portal) otherObject).parent.position.x - ((Portal) otherObject).position.x);
                            object.position.y += (((Portal) otherObject).parent.position.y - ((Portal) otherObject).position.y);

                            //object.position.add(((Portal) otherObject).parent.position);
                            object.position.x += 150;
                            object.position.wrap(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
                        } else {
                            object.position.x += (((Portal) otherObject).twin.position.x - ((Portal) otherObject).position.x);
                            object.position.y += (((Portal) otherObject).twin.position.y - ((Portal) otherObject).position.y);
                            //object.position.add(((Portal) otherObject).twin.position);

                            object.position.x += 150;
                            object.position.wrap(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
                        }
                        continue;
                    }
                    if (!(object instanceof Ship) && otherObject instanceof PowerUp) continue;
                    if (!(object instanceof Ship) && otherObject instanceof CreditPack) continue;
                    if (!(object instanceof Ship) && otherObject instanceof Portal) continue;
                    object.hit();
                    otherObject.hit();
                    return;
                }
            }
        }
    }

    public boolean overlap(GameObject x, GameObject y) {

        return x.position.dist(y.position) <= x.radius() + y.radius();
    }

    public void incScore(int sc) {
        this.score += sc;
    }

    public int getScore() {
        return this.score;
    }

    public void decreaseLives() {
        this.lives--;
    }

    public int getLives() {
        return this.lives;
    }


    public void save() {
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new
                    FileOutputStream(Constants.SAVE_FILE));
            out.writeObject(this);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Game load() {
        AsteroidsGame result = null;
        try {
            File file = Constants.SAVE_FILE;
            //SoundManager.play(SoundManager.thrust);
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(file));
            result = (AsteroidsGame) (in.readObject());
            View view = new View(result);
            //result.view = view;
            BasicKeys keys = new BasicKeys();
            //result.keys = keys;
            JFrame f = new JEasyFrameFull(view);
            result.action = result.ship.ctrl.action(result);
            f.addKeyListener((BasicKeys) result.ship.ctrl);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void populateMenu() {
        menu.add(new MenuItem("Start Game"));
        menu.add(new MenuItem("Save"));
        menu.add(new MenuItem("Load"));
        menu.add(new MenuItem("Shop"));
        menu.add(new MenuItem("Exit"));
    }

    public void populateShop() {
        shop.add(new MenuItem("Weapon Upgrade", 1000));
        shop.add(new MenuItem("Extra Life", 500));
        shop.add(new MenuItem("VaderMine", 20));
    }

    public void reset() {
        menu = new ArrayList<MenuItem>();
        populateMenu();
        this.sound = false;
        this.level = 1;
        this.over = false;
        this.score = 0;
        this.lives = 3;
        this.objects = new ArrayList<GameObject>();
        this.alive = new ArrayList<GameObject>();
        this.pending = new ArrayList<GameObject>();
        ship = new Ship(this, new BasicKeys());
        action = ship.ctrl.action(this);
        ship2 = new EnemyShip(this, new SeekNShoot());
        pending.add(ship);
        pending.add(ship2);
        for (int i = 0; i < N_INITIAL_ASTEROIDS; i++) {
            Asteroid as = new Asteroid(this);
            if (overlap(as, ship)) {
                as.position.x += 45;
            }
            pending.add(as);
        }
    }

}
