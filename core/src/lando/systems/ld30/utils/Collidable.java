package lando.systems.ld30.utils;

import com.badlogic.gdx.graphics.Color;
import lando.systems.ld30.Bullet;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public interface Collidable {
    public CollidableType getType();
    public void shotByPlayer(Color color);
    public void shotByEnemy(Color color);
    public boolean collideWithBullet(Bullet bullet);
}
