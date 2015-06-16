package battle;

import battle.controllers.GAController.StupidGAWrapper;
import battle.controllers.Human.WASDController;
import battle.controllers.RotateAndShoot;
import battle.controllers.mmmcts.MMMCTS;
import battle.controllers.mmmcts.tools.ElapsedCpuTimer;
import ga.QuadraticBowl;
import ga.SimpleRandomHillClimberEngine;

import java.util.Random;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void main(String[] args) {

        SimpleBattle battle = new SimpleBattle(true);

        BattleController player1 = new StupidGAWrapper(new double[]{2., 3., 4.});
        BattleController player2 = new RotateAndShoot();
        ElapsedCpuTimer timer = new ElapsedCpuTimer();
        battle.playGame(player1, player2);
        System.out.println("ElapsedTime: " + timer.elapsedMillis());
    }

}
