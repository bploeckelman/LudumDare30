package lando.systems.ld30.utils;

import com.badlogic.gdx.graphics.Color;
import lando.systems.ld30.Bullet;
import lando.systems.ld30.EnemyLaserShot;
import lando.systems.ld30.LaserShot;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public interface Collidable {
    public CollidableType getType();
    public void shotByPlayer(LaserShot laser);
    public void shotByEnemy(LaserShot laser);
    public boolean collideWithBullet(Bullet bullet);
    public void collideWithWorld();
    public void collisionDamage(float damage);
    public float getVelocity();
}
