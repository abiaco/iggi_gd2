package Game;

import utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;


public class Shield implements Serializable {
    double rad;
    public boolean on;
    Vector2D s,v;
    boolean dead;
    Ship ship;
    public int shieldLevel;
    public int maxShield;
    transient Image im = Constants.SHIELD_BAR;
    transient Image im1 = Constants.EMPTY_PROGRRESS_BAR;
    public Shield(Ship ship){
        rad = ship.radius() +15;
        this.ship = ship;
        s= new Vector2D(ship.position);
        v = new Vector2D((ship.velocity));
        dead = false;
        on = true;
    }
   /* public Shield(AsteroidsGame game, Ship ship){
        super(game);
        position = new Vector2D(ship.position);
        velocity = new Vector2D(ship.velocity);
        this.rad = ship.radius() +15;
        //this.ship  = ship;
        dead = false;
        on=true;*/
   // }
   // @Override
    public double radius() {
        return rad;  //To change body of implemented methods use File | Settings | File Templates.
    }

   // @Override
    public void update() {
        s = new Vector2D(ship.position);
        v = new Vector2D(ship.velocity);

    }

   //@Override
    public void draw(Graphics2D g) {
        g.setColor(Color.cyan);
        g.drawOval((int) (s.x - rad), (int) (s.y - rad), (int) rad * 2, (int) rad * 2);
       // double rot = velocity.theta() + Math.PI * 2 * Constants.DT;
        double imW = im.getWidth(null);
        double imH = im.getHeight(null);
        double im1W = im1.getWidth(null);
        double im1H = im1.getHeight(null);
        AffineTransform t1 = new AffineTransform();
        // t.rotate(rot, 0, 0);
        t1.scale(maxShield*imW/180 , maxShield*imH/60);
        t1.translate(-imW/2.0, -imH/2.0);
        AffineTransform t2 = g.getTransform();
        g.translate(s.x, s.y+26);
        g.drawImage(im1, t1, null);
        g.setTransform(t2);
        AffineTransform t = new AffineTransform();
       // t.rotate(rot, 0, 0);
        t.scale(shieldLevel*imW/200, maxShield*imH/100);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(s.x-12, s.y+40);
        g.drawImage(im, t, null);
        g.setTransform(t0);


    }
    public void hit(){
        shieldLevel --;
        if(shieldLevel == 0)on = false;
    }
}
