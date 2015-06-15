package battle;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by bedder on 15/06/2015.
 */
public class BattleLogger {
    int playerId = 0;

    PrintWriter missiles;
    PrintWriter velocity;
    PrintWriter score;


    BattleLogger(int playerId) {
        this.playerId = playerId;

        try {
            new File("missiles-" + playerId + ".csv");
            new File("velocity-" + playerId + ".csv");
            new File("score-" + playerId + ".csv");

            missiles = new PrintWriter("missiles-" + playerId + ".csv");
            velocity = new PrintWriter("velocity-" + playerId + ".csv");
            score    = new PrintWriter("score-" + playerId + ".csv");
        } catch (Exception e) {
        }
    }

    public void log(SimpleBattle battle) {
        missiles.print(-battle.getMissilesLeft(playerId) + ",");
        velocity.print(battle.getShip(playerId).v.mag() + ",");
        score.print(battle.getPoints(playerId) + ",");
    }

    public void close() {
        missiles.close();
        velocity.close();
        score.close();
    }
}
