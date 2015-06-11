package Game;

import static Game.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

import utils.Vector2D;

public class Asteroid extends GameObject {
    public final static int RADIUS = 30;
    transient Image im = Constants.ASTEROID1;
    int rad;
    AffineTransform bgTransf;
    public Asteroid() {
        super(new AsteroidsGame());
        Random r = new Random();
        position = new Vector2D(r.nextInt(), r.nextInt());
        velocity = new Vector2D(r.nextInt() %15, r.nextInt() % 15);
    }

    public Asteroid(AsteroidsGame asteroidsGame) {
        super(asteroidsGame);
        rad = RADIUS * 4;
        Random r = new Random();
        position = new Vector2D(r.nextInt(), r.nextInt());
        velocity = new Vector2D(r.nextInt() % (15 * asteroidsGame.level), r.nextInt() % (15* asteroidsGame.level));
    }
    public Asteroid(AsteroidsGame asteroidsGame, Asteroid asteroid) {
        super(asteroidsGame);
        rad = asteroid.rad;
        Random r = new Random();
        position = new Vector2D(r.nextInt()%10 + asteroid.position.x, r.nextInt()%10 + asteroid.position.y);
        velocity = new Vector2D(r.nextInt() % (15*asteroidsGame.level), r.nextInt() % (15* asteroidsGame.level));
    }

    public Asteroid(AsteroidsGame asteroidsGame, double sx, double sy, double vx, double vy) {
        super(asteroidsGame);
        position = new Vector2D(sx, sy);
        velocity = new Vector2D(vx, vy);

    }

    @Override
    public void update() {
        position.add(velocity, DT*5);
        position.wrap(WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void draw(Graphics2D g) {

        Vector2D d = new Vector2D();
        d.set(velocity);
        d.normalise();
        double rot = d.theta() + Math.PI * 2 * Constants.DT;
        double imW = im.getWidth(null);
        double imH = im.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.rotate(rot, 0, 0);
        t.scale(rad/imW, rad/imH);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(position.x, position.y);
        g.drawImage(im, t, null);
        g.setTransform(t0);
    }

    @Override
    public double radius() {
        return rad-10;

    }
    public void hit(){
        Random r  = new Random();
        rad /=2;
        if(rad >= RADIUS){
        split();
        }
        if(rad <RADIUS){
            dead=true;
            asteroidsGame.credits += Constants.CREDITS_GAIN;
            if(r.nextInt(10) == 3) asteroidsGame.add(new PowerUp(asteroidsGame,this));
        }
        asteroidsGame.incScore(Constants.SCORE_GAIN / 10);
        if(asteroidsGame.sound)SoundManager.play(SoundManager.bangMedium);

    }
    public void split(){
        asteroidsGame.add(new Asteroid(asteroidsGame,this));
        asteroidsGame.add(new Asteroid(asteroidsGame, this));
    }

}
