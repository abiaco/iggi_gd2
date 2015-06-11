package Game;

import utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

import javax.swing.*;


public class View extends JComponent {

    private static final long serialVersionUID = 1L;
    public AsteroidsGame asteroidsGame;
    Vector2D center;
    transient Image im = Constants.HEALTH_BAR;
    transient Image im2 = Constants.MILKYWAY1;
    transient Image im3 = Constants.EMPTY_PROGRRESS_BAR;
    AffineTransform bgTransf;
    boolean okDraw;
    MiniMap map;


    public View(Game game) {
        this.asteroidsGame = (AsteroidsGame) game;
        map = new MiniMap(this.asteroidsGame);
        okDraw = false;
        double imWidth = im2.getWidth(null);
        double imHeight = im2.getHeight(null);
        double stretchx = (imWidth > Constants.WORLD_WIDTH ? 1 :
                Constants.WORLD_WIDTH / imWidth);
        double stretchy = (imHeight > Constants.WORLD_HEIGHT ? 1 :
                Constants.WORLD_HEIGHT / imHeight);
        bgTransf = new AffineTransform();
        bgTransf.scale(stretchx, stretchy);
    }

    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        AffineTransform t0 = g.getTransform();


        g.setColor(Constants.BG_COLOR);
        g.fillRect(0, 0, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        if (!asteroidsGame.action.menu) {
            if (!asteroidsGame.action.shop) {
                center = asteroidsGame.getShipLocation();


                g.translate(Constants.FRAME_WIDTH / 2 - center.x,
                        Constants.FRAME_HEIGHT / 2 - center.y);

                g.drawImage(im2, bgTransf, new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });
                Font f = new Font("Poor Richard", Font.BOLD, 20);
                g.setFont(f);
                g.setColor(Color.WHITE);
                g.drawString("Lives left: " + asteroidsGame.getLives(), (int) center.x - Constants.FRAME_WIDTH / 2 + 5, (int) center.y - Constants.FRAME_HEIGHT / 2 + 30);
                g.drawString("Score: " + asteroidsGame.getScore(), (int) center.x - Constants.FRAME_WIDTH / 2 + 5, (int) center.y - Constants.FRAME_HEIGHT / 2 + 55);
                g.drawString("Level: " + asteroidsGame.level, (int) center.x - Constants.FRAME_WIDTH / 2 + 5, (int) center.y - Constants.FRAME_HEIGHT / 2 + 80);
                g.drawString("Galactic Credits: " + asteroidsGame.credits, (int) center.x - Constants.FRAME_WIDTH / 2 + 5, (int) center.y - Constants.FRAME_HEIGHT / 2 + 105);
                g.drawString("Mines left: " + asteroidsGame.ship.bombs,(int) center.x - Constants.FRAME_WIDTH / 2 + 5, (int) center.y - Constants.FRAME_HEIGHT / 2 + 130);
                g.drawString("Weapon Level: " + asteroidsGame.ship.bullet_level,(int) center.x - Constants.FRAME_WIDTH / 2 + 5, (int) center.y - Constants.FRAME_HEIGHT / 2 + 155 );
                double imW = im.getWidth(null);
                double imH = im.getHeight(null);

                //AffineTransform t = new AffineTransform();
                AffineTransform t = new AffineTransform();
                t.scale(500 * imW / 3000, 2);
                t.translate(-imW / 2.0, -imH / 3.0-21);
                AffineTransform t2 = g.getTransform();
                g.translate((int) center.x - Constants.FRAME_WIDTH / 2 + 5, (int) center.y - Constants.FRAME_HEIGHT / 2 + 170);
                g.drawImage(im3, t, null);

                g.setTransform(t2);
                t= new AffineTransform();
                t.scale(asteroidsGame.ship.hp * imW / 3000, 2);
                t.translate(-imW / 2.0, -imH / 2.0);
                t2 = g.getTransform();
                g.translate((int) center.x - Constants.FRAME_WIDTH / 2 + 5, (int) center.y - Constants.FRAME_HEIGHT / 2 + 170);
                g.drawImage(im, t, null);

                g.setTransform(t2);

                drawObjects(g, asteroidsGame.getGameObjects());

                if (asteroidsGame.ship.shield.on) asteroidsGame.ship.shield.draw(g);
                if (asteroidsGame.over) {
                    g.drawString("GAME OVER", (int) center.x - 3, (int) center.y);
                }
                g.setTransform(t0);
                synchronized (asteroidsGame) {
                    this.map.draw(g);
                }
            } else {
                g.drawImage(Constants.SHOPBKG, bgTransf, new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });
                Font f = new Font("Poor Richard", Font.BOLD, 40);
                g.setFont(f);
                for (int i = 0; i < asteroidsGame.shop.size(); i++) {
                    if (asteroidsGame.action.shopSelected != i) g.setColor(Color.WHITE);
                    else g.setColor(Color.GREEN);
                    g.drawString(asteroidsGame.shop.get(i).name, Constants.FRAME_WIDTH / 2 - 5, Constants.FRAME_HEIGHT / 4 + i * 45);
                }
            }
        } else {
            g.drawImage(Constants.MENUBKG, bgTransf, new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
            Font f = new Font("Poor Richard", Font.BOLD, 40);
            g.setFont(f);

            for (int i = 0; i < asteroidsGame.menu.size(); i++) {
                if (asteroidsGame.action.menuSelected != i) g.setColor(Color.WHITE);
                else g.setColor(Color.GREEN);
                g.drawString(asteroidsGame.menu.get(i).name, Constants.FRAME_WIDTH / 2 - 5, Constants.FRAME_HEIGHT / 4 + i * 45);
            }
        }

    }



    public void drawObjects(Graphics2D g,
                            Iterable<GameObject> objs) {
        double minx = center.x - Constants.FRAME_WIDTH / 2.0;
        double maxx = center.x + Constants.FRAME_WIDTH / 2.0;
        double miny = center.y - Constants.FRAME_HEIGHT / 2.0;
        double maxy = center.y + Constants.FRAME_HEIGHT / 2.0;
        Rectangle2D screen =
                new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny);
        synchronized (asteroidsGame) {
            for (GameObject obj : objs) {
                Vector2D s0 = new Vector2D(obj.position);
                Vector2D s = obj.position;
                if (s.x < minx) s.add(Constants.WORLD_WIDTH, 0);
                else if (s.x > maxx) s.add(-Constants.WORLD_WIDTH, 0);
                if (s.y < miny) s.add(0, Constants.WORLD_HEIGHT);
                else if (s.y > maxy) s.add(0, -Constants.WORLD_HEIGHT);
                if (obj.getBounds2D().intersects(screen)) obj.draw(g);
                obj.position = s0;
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }


}
