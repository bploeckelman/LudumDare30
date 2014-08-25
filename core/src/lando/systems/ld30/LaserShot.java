package lando.systems.ld30;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import lando.systems.ld30.utils.*;

import java.util.PriorityQueue;

/**
 * Created by dsgraham on 8/23/14.
 */
public class LaserShot {
    public float timeLeft;
    public boolean alive = false;
    public Sprite sprite;
    Vector2 target;
    Body body;
    float scale;
    float length;
    Color color;
    float angle;
    public float damage;

    PriorityQueue<RayHit> hits = new PriorityQueue<RayHit>();

    public LaserShot(Body body, Vector2 target, Color color, float TTL, float damage){
        this.damage = damage;
        timeLeft = TTL;
        alive = true;
        sprite = new Sprite(Assets.beam);
        this.target = target;
        this.body = body;
        scale = .5f;
        length = 0;
        sprite.setColor(color);
        this.color = color.cpy();
    }

    boolean active = false;
    public void update (float dt){
        timeLeft -= dt;

        if (timeLeft > 1.8f){
            scale = .1f;
        } else if (timeLeft > .2f){
            scale = .1f;
        } else if (timeLeft > 0 ) {
            // TODO: Play Laser Sound
            scale = 2f;
            active = true;
        } else {
            alive = false;
        }
    }



    public void render(SpriteBatch batch){
        Vector2 vDir = getDir();

        //Vector2 rayTarget = new Vector2(body.getPosition().x + (1000*xDif), body.getPosition().y + (1000*yDif));
        float dist = body.getPosition().dst(vDir);
        length = 100;
        hits.clear();
        Globals.world.rayCast(rayCallback, body.getPosition(), vDir);

        while (hits.peek() != null) {
            RayHit hit = hits.poll();
            if (hit.collidable == null || hit.collidable.getType() == CollidableType.WORLD){
                length = hit.fraction;
                break;
            }
            if (active){
                hitSomething(hit.collidable);
                alive = false;
            }
        }
        sprite.setSize(length * dist,1);
        sprite.setOrigin(0, sprite.getHeight()/2);

        sprite.setRotation(angle);
        sprite.setScale(1, scale);
        sprite.setPosition(body.getPosition().x, body.getPosition().y);


        sprite.draw(batch);

    }

    protected Vector2 getDir() {
        float xDif = target.x - body.getPosition().x;
        float yDif = target.y - body.getPosition().y;
        angle = (float) (180 * Math.atan2(yDif, xDif) / Math.PI);

        return new Vector2(xDif, yDif).nor().scl(100).add(body.getPosition());
    }

    protected void hitSomething(Collidable c){
        c.shotByPlayer(color);
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
