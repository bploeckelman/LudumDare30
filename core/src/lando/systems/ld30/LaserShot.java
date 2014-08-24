package lando.systems.ld30;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Collidable;
import lando.systems.ld30.utils.Globals;

/**
 * Created by dsgraham on 8/23/14.
 */
public class LaserShot {
    public float timeLeft;
    public boolean alive = false;
    public Sprite sprite;
    private Vector2 target;
    Player player;
    float scale;
    float length;
    Color color;

    public LaserShot(Player player, Vector2 target, Color color){
        timeLeft = 2f;
        alive = true;
        sprite = new Sprite(Assets.beam);
        this.target = target;
        this.player = player;
        scale = .5f;
        length = 0;
        sprite.setColor(color);
        this.color = color.cpy();
    }

    boolean active = false;
    public void update (float dt){
        timeLeft -= dt;

        if (timeLeft > 1.8f){
            scale = .5f;
        } else if (timeLeft > .2f){
            scale = .1f;
        } else if (timeLeft > 0 ) {
            scale = 2f;
            active = true;
        } else {
            alive = false;
        }
    }



    public void render(SpriteBatch batch){
        float angle;
        float xDif = target.x - player.body.getPosition().x;
        float yDif = target.y - player.body.getPosition().y;
        float dir = yDif < 0 ? 180 : 0;
        if (xDif == 0){
           angle = dir;
        } else {
            angle = (float) (180 * Math.atan2(yDif, xDif) / Math.PI);
        }
        float dist = player.body.getPosition().dst(target);
        length = 100;
        Globals.world.rayCast(rayCallback, player.body.getPosition(), target);
        sprite.setSize(length * dist,1);
        sprite.setOrigin(0, sprite.getHeight()/2);

        sprite.setRotation(angle);
        sprite.setScale(1, scale);
        sprite.setPosition(player.body.getPosition().x, player.body.getPosition().y);


        sprite.draw(batch);

    }

    private RayCastCallback rayCallback = new RayCastCallback(){
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            final Collidable collidable = (Collidable) fixture.getBody().getUserData();
            if (collidable == null) {
                length = fraction;
                return 0;
            }
            if (active) {
                collidable.shotByPlayer(color);
            }
            return 1;
        }
    };
}
