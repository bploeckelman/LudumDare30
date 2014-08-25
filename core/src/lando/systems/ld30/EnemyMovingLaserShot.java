package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import lando.systems.ld30.utils.CollidableType;
import lando.systems.ld30.utils.RayHit;

/**
 * Created by dsgraham on 8/25/14.
 */
public class EnemyMovingLaserShot extends EnemyLaserShot{
    public EnemyMovingLaserShot(Body body, Vector2 target, Color color, float TTL, float damage) {
        super(body, target, color, TTL, damage);
    }

    public void update (float dt){
        timeLeft -= dt;
        if (timeLeft > 0 ) {
            // TODO: Play Laser Sound
            scale = 2f;
            active = true;
        } else {
            alive = false;
        }
    }

    protected Vector2 getDir() {
        angle = (float) (180 * Math.atan2(target.x, target.y) / Math.PI);
        Vector2 angled = new Vector2(1,0).rotate(angle).nor().scl(200).add(body.getPosition());
        return angled;
    }

    protected void doDamage(){
        while (hits.peek() != null) {
            RayHit hit = hits.poll();
            if (hit.collidable == null || hit.collidable.getType() == CollidableType.WORLD){
                length = hit.fraction;
                break;
            }

            hitSomething(hit.collidable);

        }
    }
}
