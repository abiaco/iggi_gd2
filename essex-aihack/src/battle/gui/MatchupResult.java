package battle.gui;

/**
 * Created by mmoros on 16/06/2015.
 */
public class MatchupResult {
    public int P1Score, P2Score;
    public boolean PlayerOneWin;
    public MatchupResult(boolean PlayerOneWin, int P1Score, int P2Score)
    {
        this.PlayerOneWin = PlayerOneWin;
        this.P1Score = P1Score;
        this.P2Score = P2Score;

    }
}
