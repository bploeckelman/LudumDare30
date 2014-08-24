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
    float angle;
    float xDif;
    float yDif;

    public EnemyLaserShot(Body body, Vector2 target, Color color){
        super(body, target, color);
        xDif = target.x - body.getPosition().x;
        yDif = target.y - body.getPosition().y;
        float dir = yDif < 0 ? 180 : 0;
        if (xDif == 0){
            angle = dir;
        } else {
            angle = (float) (180 * Math.atan2(yDif, xDif) / Math.PI);
        }
    }

    private PriorityQueue<RayHit> hits = new PriorityQueue<RayHit>();

    @Override
    public void render(SpriteBatch batch){

        //Vector2 rayTarget = new Vector2(body.getPosition().x + (100*xDif), body.getPosition().y + (100*yDif));
        Vector2 vDir = new Vector2(xDif, yDif).nor().scl(100).add(body.getPosition());
        float dist = body.getPosition().dst(vDir);
        length = 1;
        hits.clear();
        Globals.world.rayCast(rayCallback, body.getPosition(), vDir);

        while (hits.peek() != null) {
            RayHit hit = hits.poll();
            if (hit.collidable == null || hit.collidable.getType() == CollidableType.WORLD){
                length = hit.fraction;
                break;
            }
            if (active){
                hit.collidable.shotByEnemy(color);
            }
        }

        sprite.setSize(length * dist,1);
        sprite.setOrigin(0, sprite.getHeight()/2);

        sprite.setRotation(angle);
        sprite.setScale(1, scale);
        sprite.setPosition(body.getPosition().x, body.getPosition().y);


        sprite.draw(batch);

    }




    protected RayCastCallback rayCallback = new RayCastCallback(){
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            final Collidable collidable = (Collidable) fixture.getBody().getUserData();
            hits.add(new RayHit(collidable, fraction));
            if (collidable == null || collidable.getType() == CollidableType.WORLD) {
                hits.add(new RayHit(collidable, fraction));
                return fraction;
            }
            return 1;
        }
    };
}
