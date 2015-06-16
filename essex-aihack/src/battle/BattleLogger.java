package battle;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bedder on 15/06/2015.
 */
public class BattleLogger {
    int playerId = 0;

    PrintWriter missiles;
    PrintWriter velocity;
    PrintWriter score;


    BattleLogger(String agent, int playerId) {
        this.playerId = playerId;
        String baseString = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        baseString = "Data/" + baseString + "-" + agent + "-" + playerId;

        try {
            new File(baseString + "-missiles.csv");
            new File(baseString + "-velocity.csv");
            new File(baseString + "-score.csv");

            missiles = new PrintWriter(baseString + "-missiles.csv");
            velocity = new PrintWriter(baseString + "-velocity.csv");
            score    = new PrintWriter(baseString + "-score.csv");
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
