package lando.systems.ld30.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public class Globals {
    public static final World world = new World(new Vector2(0,0), true);
}
