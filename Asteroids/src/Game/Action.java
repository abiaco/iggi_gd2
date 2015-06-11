package Game;

import java.io.Serializable;

public class Action implements Serializable {
	int thrust;
	int turn;
	boolean shoot;
    boolean shield;
    boolean sound;
    boolean menu;
    boolean menuUp;
    boolean menuDown;
    int menuSelected;
    boolean over;
    boolean exit;
    boolean shop;
    int shopSelected;
    boolean execute;
    boolean save;
    boolean load;
    boolean drop_bomb;
    boolean buy_bomb;
    boolean buy_weapon;
    boolean buy_life;
}
