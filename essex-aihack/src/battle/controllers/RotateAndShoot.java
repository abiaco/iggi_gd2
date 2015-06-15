package battle.controllers;

import asteroids.Action;
import battle.BattleController;
import battle.NeuroShip;
import battle.SimpleBattle;

/**
 * Created by simonlucas on 30/05/15.
 */
public class RotateAndShoot implements BattleController {

    NeuroShip ship;

    Action action;

    public RotateAndShoot() {
        action = new Action();
    }

    public void setVehicle(NeuroShip ship) {
        // just in case the ship is needed ...
        this.ship = ship;
    }

    @Override
    public Action getAction(SimpleBattle gameStateCopy, int playerId) {
        return new Action(0, 1, true);
    }
}
