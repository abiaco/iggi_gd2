package battle;

import battle.controllers.EmptyController;
import battle.controllers.Human.WASDController;
import battle.controllers.RotateAndShoot;
import battle.controllers.mmmcts.MMMCTS;
import battle.controllers.mmmcts.tools.ElapsedCpuTimer;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void main(String[] args) {

        SimpleBattle battle = new SimpleBattle(true);

        BattleController player1 = new MMMCTS();
        BattleController player2 = new EmptyController();
        ElapsedCpuTimer ecp = new ElapsedCpuTimer();
        battle.playGame(player1, player2);
        System.out.println("Time to run " + ecp.elapsedMillis());
    }

}
