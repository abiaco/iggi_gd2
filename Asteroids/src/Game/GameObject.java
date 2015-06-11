package Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import utils.*;

public abstract class GameObject implements Serializable {
    public AsteroidsGame asteroidsGame;
    public Vector2D position;
    public Vector2D velocity;
    public boolean dead;
    int distToShip;

    public GameObject() {
        this.asteroidsGame = new AsteroidsGame();
        this.position = new Vector2D();
        this.velocity = new Vector2D();
        this.dead = false;
    }

    public GameObject(AsteroidsGame asteroidsGame) {
        this.asteroidsGame = asteroidsGame;
        this.position = new Vector2D();
        this.velocity = new Vector2D();
        this.dead = false;
    }

    public GameObject(AsteroidsGame asteroidsGame, Vector2D position, Vector2D velocity) {
        this.asteroidsGame = asteroidsGame;
        this.position = position;
        this.velocity = velocity;
        this.dead = false;
    }

    public void hit() {
        this.dead = true;
    }


    public Vector2D to (GameObject other){
        return this.position.toWrapped(other.position,
                Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    }
    // distance to another game object in a wrapped world
    public double dist (GameObject other){
        return this.position.distWrapped(other.position,
                Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    }
    // difference vector - note the direction

    public abstract double radius();

    public abstract void update();
    public Rectangle2D getBounds2D(){
        return new Rectangle((int) this.position.x - Constants.RADIUS, (int) this.position.y - Constants.RADIUS, Constants.RADIUS*2, Constants.RADIUS *2);
    }

    public abstract void draw(Graphics2D g);

}
