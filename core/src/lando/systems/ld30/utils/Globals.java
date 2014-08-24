package lando.systems.ld30.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import lando.systems.ld30.LudumDare30;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public class Globals {
    public static final World world = new World(new Vector2(0,0), true);
    public static LudumDare30 game;

    public static final float world_center_x = 1000;
    public static final float world_center_y = 1000;
}
