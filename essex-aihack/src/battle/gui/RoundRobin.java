package battle.gui;

import battle.BattleController;
import battle.SimpleBattle;
import battle.controllers.EmptyController;
import battle.controllers.RotateAndShoot;
import battle.controllers.mmmcts.MMMCTS;
import battle.controllers.mmmcts.tools.ElapsedCpuTimer;
import battle.controllers.mmmcts2.MMMCTS2;

import javax.naming.ldap.Control;
import java.util.ArrayList;

/**
 * Created by mmoros on 16/06/2015.
 */
public class RoundRobin {

    public static double BattleBetweenControllers(BattleController p1, BattleController p2, int GameMode)
    {

        SimpleBattle battle = new SimpleBattle(false, true, GameMode);

        ElapsedCpuTimer ecp = new ElapsedCpuTimer();

        battle.playGame(p1, p2);

        return ecp.elapsedMillis();
    }



    public static void main(String[] args) {
        ArrayList<Class> ControllersToTest = new ArrayList<>();
        ControllersToTest.add(MMMCTS.class);
        ControllersToTest.add(MMMCTS2.class);
        ControllersToTest.add(EmptyController.class);
        ControllersToTest.add(RotateAndShoot.class);

        int maximumRuns = 30;

        for(int i=0;i<maximumRuns;i++) {
            for(int c1 = 0; c1 < ControllersToTest.size(); c1++)
            {
                for(int c2 = c1+1; c2 < ControllersToTest.size(); c2++)
                {
                    Class controllerClass = ControllersToTest.get(c1);
                    Class otherControllerClass = ControllersToTest.get(c2);
                    try {
                        System.out.println("Fighting: " + controllerClass.getSimpleName() + " vs " + otherControllerClass.getSimpleName());
                        System.out.println(BattleBetweenControllers((BattleController) controllerClass.newInstance(), (BattleController) otherControllerClass.newInstance(), Integer.valueOf(args[0])));
                    } catch (Exception exc) {
                    }
                }
            }
        }
    }
}
