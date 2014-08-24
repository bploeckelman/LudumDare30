package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import lando.systems.ld30.utils.Collidable;
import lando.systems.ld30.utils.Globals;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public class EnemyLaserShot extends LaserShot {

    public EnemyLaserShot(Body body, Vector2 target, Color color){
        super(body, target, color);
    }

    @Override
    public void render(SpriteBatch batch){
        float angle;
        float xDif = target.x - body.getPosition().x;
        float yDif = target.y - body.getPosition().y;
        float dir = yDif < 0 ? 180 : 0;
        if (xDif == 0){
            angle = dir;
        } else {
            angle = (float) (180 * Math.atan2(yDif, xDif) / Math.PI);
        }
        Vector2 rayTarget = new Vector2(body.getPosition().x + (100*xDif), body.getPosition().y + (100*yDif));
        float dist = body.getPosition().dst(rayTarget);
        length = 100;
        Globals.world.rayCast(rayCallback, body.getPosition(), rayTarget);
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
            if (collidable == null) {
                length = fraction;
                return 0;
            }
            if (active) {
                collidable.shotByEnemy(color);
            }
            return -1;
        }
    };
}
