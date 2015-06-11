package Game;

import utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class EnemyShip extends GameObject {
    static final int RADIUS = 30;
    static final double STEER_RATE = Math.PI;  // in radians per second
    static final double LOSS = 0.99;
    static final double SHIP_VMAX = 200;
    static int flag;
    int hp;
    boolean fleeing;
    EnemyController ctrl;
    transient Image im = Constants.SHIP2;
    Vector2D d;

    public EnemyShip(AsteroidsGame asteroidsGame, EnemyController ctrl) {
        super(asteroidsGame);
        dead = false;
        hp = 100;
        fleeing = false;
        this.ctrl = ctrl;
        position = new Vector2D();
        velocity = new Vector2D();
        d = new Vector2D();
        flag = 0;
        reset();
    }


    public void reset() {
        Random r = new Random();
        position = new Vector2D(r.nextInt((int) Constants.WORLD_WIDTH), r.nextInt((int) Constants.WORLD_HEIGHT));
        velocity = new Vector2D(0, 0);
        d = new Vector2D(0, -1);
        d.normalise();
    }

    @Override
    public void update() {
        Action action = ctrl.action(asteroidsGame, this);
        double theta = d.theta();
        theta += action.turn * STEER_RATE * Constants.DT;
        d.set(Math.cos(theta), Math.sin(theta));
        d.normalise();
        if (action.thrust == 0) velocity.set(0, 0);
        else {
            if (velocity.mag() < SHIP_VMAX) velocity.add(d, 15);
        }
        velocity.multiply(LOSS);
        position.add(velocity, Constants.DT);
        position.wrap(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        if (action.shoot) {
            if (flag == 10) {
                flag = 0;
                shootBullet();
            } else flag++;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        double rot = d.theta() + Math.PI * 2 * Constants.DT;

        double imW = im.getWidth(null);
        double imH = im.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.rotate(rot, 0, 0);
        t.scale(RADIUS * 3 / imW, RADIUS * 3 / imH);
        t.translate(-imW / 2.0, -imH / 2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(position.x, position.y);
        g.drawImage(im, t, null);
        g.setTransform(t0);
    }

    private void shootBullet() {
        Vector2D bV = new Vector2D(velocity);
        bV.add(d, SHIP_VMAX);
        Bullet b = new Bullet(new Vector2D(position), bV);
        // make it clear the ship
        b.position.add(b.velocity, (RADIUS + b.radius()) * 1.5 / b.velocity.mag());
        asteroidsGame.add(b);
        if(this.asteroidsGame.sound)SoundManager.fire();
    }

    @Override
    public double radius() {
        return RADIUS - 5;
    }


    public void hit(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            dead = true;
            asteroidsGame.credits += Constants.CREDITS_GAIN * 3;
            asteroidsGame.incScore(Constants.SCORE_GAIN);
            if (this.asteroidsGame.sound ) SoundManager.play(SoundManager.bangMedium);
        }
    }
}
