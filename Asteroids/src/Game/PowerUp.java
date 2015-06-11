package Game;

import utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class PowerUp extends GameObject {
    public static final int RADIUS = 50;
    public transient Image im = Constants.POWER_UP1;

    public PowerUp(){
        super(new AsteroidsGame());
        position =new Vector2D();
        velocity =new Vector2D();
        dead=false;
    }
    public PowerUp(AsteroidsGame game, Asteroid as){
        super(game);
        position = new Vector2D(as.position);
        velocity = new Vector2D(0,0);
        dead=false;
    }
    @Override
    public double radius() {
        return RADIUS;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        double imW = im.getWidth(null);
        double imH = im.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.scale(RADIUS/imW, RADIUS/imH);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(position.x, position.y);
        g.drawImage(im, t, null);
        g.setTransform(t0);
    }
}
