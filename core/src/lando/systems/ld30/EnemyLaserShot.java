package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import lando.systems.ld30.utils.Collidable;
import lando.systems.ld30.utils.CollidableType;
import lando.systems.ld30.utils.Globals;
import lando.systems.ld30.utils.RayHit;

import java.util.PriorityQueue;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public class EnemyLaserShot extends LaserShot {
    float xDif;
    float yDif;


    public EnemyLaserShot(Body body, Vector2 target, Color color, float TTL, float damage){
        super(body, target, color, TTL, damage);
        xDif = target.x - body.getPosition().x;
        yDif = target.y - body.getPosition().y;
        float dir = yDif < 0 ? 180 : 0;
        if (xDif == 0){
            angle = dir;
        } else {
            angle = (float) (180 * Math.atan2(yDif, xDif) / Math.PI);
        }
    }



    protected Vector2 getDir() {
        return new Vector2(xDif, yDif).nor().scl(200).add(body.getPosition());
    }

    protected void hitSomething(Collidable c){
        c.shotByEnemy(this);
    }





}
