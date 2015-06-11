package Game;

import utils.*;

import java.io.Serializable;

public class SeekNShoot implements EnemyController, Serializable {
    public static final double FLEE_ANGLE = 40;
    public static final double FLEE_DISTANCE = Constants.DT * 5;
    public static final int FLEE_THRUST = 40;
    public static final double PURSUIT_ANGLE = Math.PI / 12;
    public static final int PURSUIT_THRUST = 100;
    public static final int PURSUIT_DISTANCE = 1900;
    public static final double SHOOTING_ANGLE = Math.PI / 12;
    public static final double SHOOTING_DISTANCE = 100;

    // Action action;
    EnemyShip ship;
    GameObject target;
    Action action = new Action();

    public Action action(Game game, EnemyShip ship) {
        this.ship = ship;
        //GameObject nextTarget = findTarget(game.getGameObjects());
        GameObject nextTarget = ((AsteroidsGame) game).ship;
        if (nextTarget == null)
            return action;
        switchTarget(nextTarget);
        if (ship.dist(nextTarget) < FLEE_DISTANCE)
            evade();
        else
            seek();
        action.shoot = ((Math.abs(angleToTarget()) < SHOOTING_ANGLE)
                && inShootingDistance());
        return action;
    }

    public GameObject findTarget(
            Iterable<GameObject> gameObjects) {
        double minDistance = Constants.WORLD_WIDTH;
        GameObject closestTarget = null;
        for (GameObject obj : gameObjects) {
            if (obj == ship || obj instanceof Bullet) continue;
            double dist = ship.dist(obj);
            obj.distToShip = (int) dist;
            if (dist < minDistance) {
                closestTarget = obj;
                minDistance = dist;
            }
        }
        return closestTarget;
    }

    // turn towards target
// apply thrust if ship is pointing roughly at target and
// the target is outside of the shooting distance
    public void seek() {
        ship.fleeing = false;
        double angle = angleToTarget();
        action.turn = (int) Math.signum(Math.sin(angle));
        if ((Math.abs(angle) < PURSUIT_ANGLE) &&
                !inShootingDistance())
            action.thrust = 1;
        else
            action.thrust = 0;
    }

    // rotate away from target
// if pointing away, apply thrust
    public void evade() {
        ship.fleeing = true;
        double angle = angleToTarget();
        if (Math.abs(angle) < FLEE_ANGLE) {
            action.turn = -(int) Math.signum(Math.sin(angle));
            action.thrust = 0;
        } else {
            action.thrust = 1;
        }
    }

    public double angleToTarget() {
        Vector2D v = ship.to(target);
        v.rotate(-ship.d.theta());
        return v.theta();
    }

    public boolean inShootingDistance() {
        return ship.dist(target) < SHOOTING_DISTANCE + target.radius();
    }

    public void switchTarget(GameObject newTarget) {
        target = newTarget;
    }
}
