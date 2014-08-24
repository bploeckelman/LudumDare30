package lando.systems.ld30.utils;

import com.badlogic.gdx.graphics.Color;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public interface Collidable {
    public CollidableType getType();
    public void ShotByPlayer(Color color);
}
