package Game;

import java.awt.*;
import java.awt.geom.AffineTransform;

import utils.Vector2D;

public class Ship extends GameObject {
    static final int RADIUS = 30;
    static final double STEER_RATE =  Math.PI;  // in radians per second
    static final double LOSS = 0.99;
    static final double SHIP_VMAX = 200;
    static int flag;
    static int bombflag;
    int bombs;
    int bullet_level;
    int hp;
    Controller ctrl;
    transient Image im;
    Vector2D d;
    Shield shield;
    public Ship(AsteroidsGame asteroidsGame, Controller ctrl) {
        super(asteroidsGame);
        im =Constants.SHIP;
        this.bombs = 3;
        hp = 500;
        bullet_level = 1;
        shield = new Shield(this);
        shield.on=false;
        this.shield.maxShield = 5;
        this.shield.shieldLevel = 1;

        this.ctrl = ctrl;
        position = new Vector2D();
        velocity = new Vector2D();
        d = new Vector2D();
        flag = 0;
        bombflag = 0;
        reset();
    }


    public void reset() {
        position = new Vector2D(Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT / 2);
        velocity = new Vector2D(0, 0);
        d = new Vector2D(0, -1);
        hp = 500;
        d.normalise();
    }

    @Override
    public void update()  {
        Action action = ctrl.action(asteroidsGame);
        if(!action.menu){
        if(shield.shieldLevel==0)shield.on = false;
        else shield.on = action.shield;
        this.shield.update();
        asteroidsGame.sound = action.sound;
            if(action.drop_bomb){
                if(bombs > 0){
                if(bombflag == 10){asteroidsGame.add(new VaderMine(asteroidsGame,this));
                bombs --; bombflag = 0;}
                else bombflag ++;}
            }
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
        if(action.shoot){
            if(flag == 10)
            {   flag=0;
                shootBullet();
            }
            else flag++;
        }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        double rot = d.theta() + Math.PI * 2 * Constants.DT;

        double imW = im.getWidth(null);
        double imH = im.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.rotate(rot, 0, 0);
        t.scale(RADIUS*3/imW, RADIUS*3/imH);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(position.x, position.y);
        g.drawImage(im, t, null);
        g.setTransform(t0);
    }
    private void shootBullet()  {
        Vector2D bV = new Vector2D(velocity);
        bV.add(d, SHIP_VMAX);
        if(bullet_level == 1){
            Bullet b = new Bullet(new Vector2D(position), bV);
            // make it clear the ship
            b.position.add(b.velocity, (RADIUS + b.radius()) * 1.5 / b.velocity.mag());
            asteroidsGame.add(b);    }
        if(bullet_level == 2){
            Bullet b1 = new Bullet(new Vector2D(position.x-5, position.y), bV);
            Bullet b2 = new Bullet(new Vector2D(position.x +5, position.y), bV);
            b1.position.add(b1.velocity, (RADIUS + b1.radius()) * 1.5 / b1.velocity.mag());
            b2.position.add(b2.velocity, (RADIUS + b2.radius()) * 1.5 / b2.velocity.mag());
            asteroidsGame.add(b1);
            asteroidsGame.add(b2);
        }
        if(bullet_level == 3){
            Bullet b1 = new Bullet(new Vector2D(position.x-10, position.y), bV);
            Bullet b2 = new Bullet(new Vector2D(position.x +10, position.y), bV);
            Bullet b = new Bullet(new Vector2D(position), bV);
            b1.position.add(b1.velocity, (RADIUS + b1.radius()) * 1.5 / b1.velocity.mag());
            b2.position.add(b2.velocity, (RADIUS + b2.radius()) * 1.5 / b2.velocity.mag());
            b.position.add(b.velocity, (RADIUS + b.radius()) * 1.5 / b.velocity.mag());
            asteroidsGame.add(b);
            asteroidsGame.add(b1);
            asteroidsGame.add(b2);
        }
        if(this.asteroidsGame.sound)SoundManager.fire();
    }

    @Override
    public double radius() {
        return RADIUS - 5;
    }
    public void hit(){
        asteroidsGame.decreaseLives();
        reset();
        if(asteroidsGame.getLives() <= 0){
            dead = true;
            asteroidsGame.over = true;
            SoundManager.play(SoundManager.bangMedium);
        }
    }
    public void hit(int dmg){
        if(!shield.on || shield.shieldLevel == 0){
        hp -= dmg;
        }
        else if(shield.on && shield.shieldLevel >0)shield.shieldLevel -- ;

        if( hp<=0){ asteroidsGame.decreaseLives();
            reset();}
        if(asteroidsGame.getLives() <= 0){
            dead = true;
            asteroidsGame.over = true;
            if(this.asteroidsGame.sound)SoundManager.play(SoundManager.bangMedium);
        }
    }
}
