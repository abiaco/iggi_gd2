package Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

public class BasicKeys extends KeyAdapter implements Controller, Serializable {
    Action action;

    public BasicKeys() {
        action = new Action();
    }

    public static Controller adapter(
            final BasicController bc) {
        return new Controller() {
            @Override
            public Action action(Game game) {
                return bc.action();
            }
        };
    }

    public Action action(Game game) {
        return action;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                if (action.menu) {
                    if (action.menuSelected >0) action.menuSelected--;
                    else action.menuSelected = 4;
                }
                else if(action.shop){
                    if(action.shopSelected > 0) action.shopSelected --;
                    else action.shopSelected = 2;
                } else action.thrust = 1;
                break;
            case KeyEvent.VK_LEFT:
                action.turn = -1;
                break;
            case KeyEvent.VK_RIGHT:
                action.turn = +1;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = true;
                break;
            case KeyEvent.VK_E:
                action.shield = true;
                break;
            case KeyEvent.VK_M:
                if (!action.sound) action.sound = true;
                else action.sound = false;
                break;
            case KeyEvent.VK_ESCAPE:
                if (!action.menu){
                    if(!action.shop)action.menu = true;
                    else {
                        action.shop = false;
                        action.menu = true;
                    }
                }
                else action.menu = false;

                break;
            case KeyEvent.VK_DOWN:
                if (action.menu) {
                    if (action.menuSelected < 4) action.menuSelected++;
                    else action.menuSelected = 0;
                }
                else if(action.shop){
                    if(action.shopSelected < 2) action.shopSelected ++;
                    else action.shopSelected = 0;
                }
                break;
            case KeyEvent.VK_ENTER:
                if(action.menu){
                    if(action.menuSelected == 0 && !action.over){

                        action.menu=false;
                    }
                    if(action.over && action.menuSelected==0){
                        action.over = false;
                        action.menu = false;
                    }
                    if(action.menuSelected == 1 && !action.over){
                        action.save = true;
                    }
                    if(action.menuSelected == 2){
                        action.load = true;
                    }
                    if(action.menuSelected == 3){
                        action.menu = false;
                        action.shop = true;
                    }
                    if(action.menuSelected == 4){ System.exit(0);
                        action.exit = true;
                    }

                }
                if(action.shop){
                    if(action.shopSelected == 0){
                        action.buy_weapon = true;
                    }
                    if(action.shopSelected == 1){
                        action.buy_life = true;
                    }
                    if(action.shopSelected == 2){
                        action.buy_bomb = true;
                    }
                }
                break;
            case KeyEvent.VK_CONTROL :
                action.drop_bomb = true;
                break;
        }

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                if (action.menu) {
                    action.menuUp = false;
                } else action.thrust = 0;
                break;
            case KeyEvent.VK_LEFT:
                action.turn = 0;
                break;
            case KeyEvent.VK_RIGHT:
                action.turn = 0;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = false;
                break;
            case KeyEvent.VK_E:
                action.shield = false;
                break;
            case KeyEvent.VK_DOWN:
                if (action.menu) {
                    action.menuDown = false;
                }
                break;
            case KeyEvent.VK_CONTROL :
                action.drop_bomb = false;
                break;
        }

    }

}
