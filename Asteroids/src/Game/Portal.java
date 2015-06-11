package Game;


import utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Portal extends GameObject{
    public static final int RADIUS = 70;
    public transient Image im = Constants.WARP;
    public Portal twin;
    public Portal parent;
    boolean twinOne;
    public Portal(){
        super(new AsteroidsGame());
        position =new Vector2D();
        velocity =new Vector2D();
        dead=false;
    }
    public Portal(AsteroidsGame game){
        super(game);
        twinOne = false;
        Random r = new Random();
        this.twin = null;
        position = new Vector2D(r.nextInt((int) Constants.WORLD_WIDTH),r.nextInt((int) Constants.WORLD_HEIGHT));
        velocity = new Vector2D(0,0);
        dead=false;
    }
    public Portal(AsteroidsGame game, Portal parent){
        super(game);
        this.parent = parent;
        twinOne = true;
        Random r = new Random();
        position = new Vector2D(r.nextInt((int) Constants.WORLD_WIDTH),r.nextInt((int) Constants.WORLD_HEIGHT));
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
        t.scale((RADIUS+60)/imW, (RADIUS+60)/imH);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(position.x, position.y);
        g.drawImage(im, t, null);
        g.setTransform(t0);
    }
}
