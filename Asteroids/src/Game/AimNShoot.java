package Game;

import utils.Vector2D;

import java.io.Serializable;


public class AimNShoot implements EnemyController, Serializable {

    public static final double SHOOTING_DISTANCE = 150;
    public static final double SHOOTING_ANGLE = Math.PI / 12;
    AsteroidsGame game;
    GameObject target;
    Action action = new Action();
   // Ship ship;
    EnemyShip ship;


    @Override
    public Action action(Game game, EnemyShip ship) {
        this.ship = ship;
       // GameObject nextTarget = findTarget(game.getGameObjects());
        GameObject nextTarget = ((AsteroidsGame) game).ship;
        if (nextTarget == null)
            return action;
        switchTarget(nextTarget);
        aim();
        action.shoot = ((Math.abs(angleToTarget()) < SHOOTING_ANGLE)
                && inShootingDistance());
        return action;
    }
    public GameObject findTarget(Iterable<GameObject> gameObjects){
        double minDistance = 2 * SHOOTING_DISTANCE;
        GameObject closestTarget = null;
        for (GameObject obj : gameObjects) {
            if (obj == ship || obj instanceof Bullet )
                continue;
            double dist = ship.dist(obj);
            //double dist = 50;
            if (dist < minDistance) {
                closestTarget = obj;
                minDistance = dist;
            }
        }
        return closestTarget;
    }
    public double angleToTarget() {
        Vector2D v = ship.to(target);
        v.rotate(-ship.d.theta());
        return v.theta();
    }
    public boolean inShootingDistance() {
        return  ship.dist(target) < SHOOTING_DISTANCE + target.radius(); }

    public void aim() {
        double angle = angleToTarget();
        action.turn = (int) Math.signum(Math.sin(angle));
    }

    public void switchTarget(GameObject newTarget) {
        target = newTarget;
    }
}
