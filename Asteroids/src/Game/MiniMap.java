package Game;

import java.awt.*;

public class MiniMap {

    AsteroidsGame game;

    public MiniMap(AsteroidsGame game){
        this.game = game;
    }

    public void draw (Graphics g0){
        Graphics2D g = (Graphics2D) g0;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(Constants.MINIMAP_X, Constants.MINIMAP_Y, Constants.MINIMAP_WIDTH, Constants.MINIMAP_HEIGHT);

        for (GameObject obj : this.game.objects){
            int x = (int) (obj.position.x * Constants.MINIMAP_WIDTH / Constants.WORLD_WIDTH);
            int y = (int) (obj.position.y * Constants.MINIMAP_HEIGHT / Constants.WORLD_HEIGHT);
            x += Constants.MINIMAP_X;
            y += Constants.MINIMAP_Y;
            if (obj instanceof EnemyShip) {
                g.setColor(Color.red);
                g.fillOval(x, y, 10, 10);
            }
            else if (obj instanceof  Asteroid){
                g.setColor(Color.green);
                g.fillOval(x, y, 10, 10);
            }
            else if (obj instanceof  PowerUp || obj instanceof CreditPack){
                g.setColor(Color.yellow);
                g.fillOval(x, y, 7, 7);
            }
            else if (obj instanceof Portal) {
                g.setColor(Color.ORANGE);
                g.fillRect(x, y, 8, 8);
            }
            else if (obj instanceof  Bullet){
                continue;
            }
            else if (obj instanceof  Ship){
                g.setColor(Color.blue);
                g.fillArc(x, y, 25, 25, 45, 90);
            }
            else {
                g.setColor(Color.magenta);
                g.fillOval(x, y, 5, 5);
            }
        }
    }
}
