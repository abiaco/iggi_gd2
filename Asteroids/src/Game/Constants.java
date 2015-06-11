package Game;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Constants {
	public static final Color BG_COLOR = Color.BLACK;
	public static final int FRAME_HEIGHT = 1080;
	public static final int FRAME_WIDTH = 1920;
    public static final double WORLD_HEIGHT = FRAME_HEIGHT *2;
    public static final double WORLD_WIDTH = FRAME_WIDTH *2;
	public static final Dimension FRAME_SIZE = 
			new Dimension(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
    public static final int MINIMAP_WIDTH = 200;
    public static final int MINIMAP_HEIGHT = 200;
    public static final int MINIMAP_X = FRAME_WIDTH - 300;
    public static final int MINIMAP_Y = FRAME_HEIGHT - 300;
	public static final int DELAY = 10;
	public static double DT = DELAY/1000.0;
	public static final int N_INITIAL_ASTEROIDS = 5;
	public static final int VMAX = 100;
	public static int RADIUS = 30;
    public static File SAVE_FILE = new File("saveGame.dat");
    public static final int WEAPON_PRICE = 1000;
    public static final int BOMB_PRICE = 20;
    public static final int LIFE_PRICE = 500;
    public static final int SCORE_GAIN = 1000;
    public static final int CREDITS_GAIN = 20;
    public static transient Image ASTEROID1, MILKYWAY1,SHIP,POWER_UP1, COIN, PROGRESS_BAR,SHIELD_BAR,SHIP2, EMPTY_PROGRRESS_BAR,HEALTH_BAR,WARP,BOMB,MENUBKG, SHOPBKG;
    static {
        try {
            ASTEROID1 = ImageManager.loadImage("deathst2");
            MILKYWAY1 = ImageManager.loadImage("background2");
            SHIP = ImageManager.loadImage("ship7");
            SHIP2 = ImageManager.loadImage("enemy");
            POWER_UP1 = ImageManager.loadImage("shield2");
            SHIELD_BAR = ImageManager.loadImage("progressBarGreen");
            EMPTY_PROGRRESS_BAR = ImageManager.loadImage("progressBar");
            HEALTH_BAR = ImageManager.loadImage("healthBar");
            COIN = ImageManager.loadImage("coin");
            WARP =ImageManager.loadImage(("porta"));
            BOMB = ImageManager.loadImage("vdmine");
            MENUBKG = ImageManager.loadImage("death-star-destroyed");
            SHOPBKG = ImageManager.loadImage("dswall");
        } catch (IOException e) { System.exit(1); }
    }
}
