package battle.gui;

import battle.BattleController;
import battle.SimpleBattle;
import battle.controllers.EmptyController;
import battle.controllers.RotateAndShoot;
import battle.controllers.mmmcts.MMMCTS;
import battle.controllers.mmmcts.tools.ElapsedCpuTimer;
import battle.controllers.mmmcts2.MMMCTS2;

import java.util.ArrayList;

/**
 * Created by mmoros on 16/06/2015.
 */
public class RoundRobin {

    public static double BattleBetweenControllers(BattleController p1, BattleController p2)
    {
        SimpleBattle battle = new SimpleBattle(false);

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

        for(Class controllerClass : ControllersToTest)
        {
            for(Class otherControllerClass : ControllersToTest)
            {
                try {
                    System.out.println("Fighting: " + controllerClass.getSimpleName() + " vs " + otherControllerClass.getSimpleName());
                    System.out.println(BattleBetweenControllers((BattleController) controllerClass.newInstance(), (BattleController) otherControllerClass.newInstance()));
                } catch (Exception exc) {}
            }
        }
    }
}
