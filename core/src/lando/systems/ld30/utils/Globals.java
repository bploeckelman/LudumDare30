package lando.systems.ld30.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.screens.GameScreen;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public class Globals {

    public static enum COLORS {RED, YELLOW, GREEN, CYAN, BLUE, PURPLE};

    public static final World world = new World(new Vector2(0,0), true);
    public static LudumDare30 game;
    public static GameScreen gameScreen;

    public static final float world_center_x = 1000;
    public static final float world_center_y = 1000;

    public static final float chamber_w = 175;
    public static final float chamber_h = 200;

    public static final float level_min_x = 640;
    public static final float level_min_y = 640;
    public static final float level_max_x = 1360;
    public static final float level_max_y = 1360;

    public static final Vector2 red_center    = new Vector2(level_max_x - chamber_w / 2 + 35,  world_center_y);
    public static final Vector2 yellow_center = new Vector2(level_max_x - chamber_w / 2 - 125, world_center_y - 265);
    public static final Vector2 green_center  = new Vector2(level_min_x + chamber_w / 2 + 125, world_center_y - 265);
    public static final Vector2 cyan_center   = new Vector2(level_min_x + chamber_w / 2 - 35,  world_center_y);
    public static final Vector2 blue_center   = new Vector2(level_min_x + chamber_w / 2 + 125, world_center_y + 265);
    public static final Vector2 purple_center = new Vector2(level_max_x - chamber_w / 2 - 125, world_center_y + 265);
}
