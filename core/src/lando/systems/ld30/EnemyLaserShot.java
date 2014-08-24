package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import lando.systems.ld30.utils.Collidable;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public class EnemyLaserShot extends LaserShot {

    public EnemyLaserShot(Body body, Vector2 target, Color color){
        super(body, target, color);
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
            return 1;
        }
    };
}
