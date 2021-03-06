package battle.controllers.Human;

import asteroids.Action;
import battle.BattleController;
import battle.SimpleBattle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by memo on 12/06/2015.
 */
public class WASDController implements BattleController, KeyListener {
    public static final Action FORWARD = new Action(1, 0, false);
    public static final Action LEFT = new Action(0, -1, false);
    public static final Action RIGHT = new Action(0, 1, false);
    public static final Action FIRE = new Action(0, 0, true);
    public static final Action NOOP = new Action(0, 0, false);

    Action currentAction = NOOP;

    @Override
    public Action getAction(SimpleBattle gameStateCopy, int playerId) {
        return currentAction;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                currentAction.thrust = 1;
                break;

            case KeyEvent.VK_A:
                currentAction.turn = -1;
                break;

            case KeyEvent.VK_D:
                currentAction.turn = 1;
                break;

            case KeyEvent.VK_SPACE:
                currentAction.shoot = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                currentAction.thrust = 0;
                break;

            case KeyEvent.VK_A:
                currentAction.turn = 0;
                break;

            case KeyEvent.VK_D:
                currentAction.turn = 0;
                break;

            case KeyEvent.VK_SPACE:
                currentAction.shoot = false;
                break;
        }
    }
}