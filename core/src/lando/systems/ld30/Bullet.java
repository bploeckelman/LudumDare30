package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.*;

/**
 * Created by dsgraham on 8/23/14.
 */
public class Bullet implements Collidable{

    public Body body;
    public Sprite sprite;
    protected static final BodyDef bodyDef = new BodyDef();
    public float body_radius = .75f;
    public Color color;
    public float TTL;
    public boolean alive;

    public Bullet(Vector2 position, Vector2 dir, Color color, boolean fromPlayer, float speed){
        this.color = color;
        alive = true;
        TTL = 30f;
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(body_radius);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = Globals.world.createBody(bodyDef);
        FixtureDef playerFixture = new FixtureDef();
        playerFixture.shape = circleShape;
        playerFixture.density = 10f;
        if (fromPlayer) {
            playerFixture.filter.categoryBits = Box2dContactListener.CATEGORY_PLAYER_BULLET;
            playerFixture.filter.maskBits = Box2dContactListener.MASK_PLAYER_BULLET;
        } else {
            playerFixture.filter.categoryBits = Box2dContactListener.CATEGORY_BULLET;
            playerFixture.filter.maskBits = Box2dContactListener.MASK_BULLET;
        }
        body.createFixture(playerFixture);
        body.setLinearDamping(0);
        body.setAngularDamping(2f);
        body.setUserData(this);
        body.applyLinearImpulse(dir.nor().scl(speed), new Vector2(0,0), true);
        circleShape.dispose();

        sprite = new Sprite(Assets.beam);
        sprite.setOriginCenter();
        sprite.setSize(2*body_radius, 2*body_radius);
    }

    public void update(float dt){
        TTL -= dt;
        if (TTL < 0){
            die();
        }
    }

    public void die()
    {
        alive = false;

    }

    public void render(SpriteBatch batch)
    {
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        sprite.setColor(color);
        sprite.draw(batch);
    }

    @Override
    public CollidableType getType() {
        return CollidableType.BULLET;
    }

    @Override
    public void shotByPlayer(Color color) {

    }

    @Override
    public void shotByEnemy(LaserShot laser) {

    }

    @Override
    public boolean collideWithBullet(Bullet bullet) {
          return false;
    }
}
