package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by dsgraham on 8/25/14.
 */
public class EnemyMovingLaserShot extends EnemyLaserShot{
    public EnemyMovingLaserShot(Body body, Vector2 target, Color color, float TTL, float damage) {
        super(body, target, color, TTL, damage);
    }

    protected Vector2 getDir() {
        return target.nor().scl(100).add(body.getPosition());
    }
}
