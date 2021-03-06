package battle.controllers.mmmcts2;

import asteroids.Action;
import battle.BattleController;
import battle.SimpleBattle;
import battle.controllers.mmmcts2.tools.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ssamot
 * Date: 14/11/13
 * Time: 21:45
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class MMMCTS2 implements BattleController {

    public static int NUM_ACTIONS;
    public static int ROLLOUT_DEPTH = 16;
    public static double K = Math.sqrt(2);
    public static ArrayList<MacroAction> actions;
    private ArrayList<Action> actionQueue;

    public static int MACRO_ACTION_LENGTH = 3;
    public static int TIMETOTHINK = 20;

    /**
     * Random generator for the agent.
     */
    private SingleMCTSPlayer mctsPlayer;

    public MMMCTS2()
    {
        actions = new ArrayList<MacroAction>();
        actionQueue = new ArrayList<>();

        //Get the actions in a static array.
        for(int i = MacroAction.ACTION_NO_FRONT; i <= MacroAction.ACTION_THR_RIGHT_SHOOT; ++i)  //6 actions
        //for(int i = Controller.ACTION_THR_FRONT; i <= Controller.ACTION_THR_RIGHT; ++i)   //Only 3 actions
        {
            boolean t = MacroAction.getThrust(i);
            int s = MacroAction.getTurning(i);
            boolean sh = MacroAction.getShoot(i);
            actions.add(new MacroAction(t,s,sh,MACRO_ACTION_LENGTH));
        }

        NUM_ACTIONS = actions.size();

        //Create the player.
        mctsPlayer = new SingleMCTSPlayer(new Random());
    }

    public Action getAction(SimpleBattle gameStateCopy, int playerId) {
        if (actionQueue.size() == 0) {
            //ArrayList<Observation> obs[] = stateObs.getFromAvatarSpritesPositions();
            //ArrayList<Observation> grid[][] = stateObs.getObservationGrid();

            //Set the state observation object as the new root of the tree.
            mctsPlayer.init(gameStateCopy, playerId);

            ElapsedCpuTimer timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(TIMETOTHINK);

            //Determine the action using MCTS...
            int actionInt = mctsPlayer.run(timer);
            Action action = actions.get(actionInt).buildAction();
            Action subsequentActions = new Action(action.thrust, action.turn, false);

            //... and return it.
            actionQueue.add(action);
            for (int i=1 ; i<MACRO_ACTION_LENGTH ; i++)
                actionQueue.add(subsequentActions);
        }
        Action action = actionQueue.get(0);
        actionQueue.remove(0);
        return action;
    }

}
