package battle;

import battle.controllers.Human.WASDController;
import battle.controllers.mmmcts.MMMCTS;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void main(String[] args) {

        SimpleBattle battle = new SimpleBattle();

        BattleController player1 = new MMMCTS();
        BattleController player2 = new WASDController();
        battle.playGame(player1, player2);
    }

}
