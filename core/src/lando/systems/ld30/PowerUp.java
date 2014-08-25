package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import lando.systems.ld30.utils.*;

/**
 * Created by dsgraham on 8/25/14.
 */
public class PowerUp implements Collidable{


    public Body body;
    public Sprite sprite;
    protected static final BodyDef bodyDef = new BodyDef();
    public float body_radius = 8f;

    public PowerUp(Vector2 pos){

        sprite = new Sprite(Assets.beam);
        sprite.setSize(2*body_radius,2*body_radius);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(body_radius);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);

        body = Globals.world.createBody(bodyDef);
        FixtureDef playerFixture = new FixtureDef();
        playerFixture.shape = circleShape;
        playerFixture.density = 10f;
        playerFixture.filter.categoryBits = Box2dContactListener.CATEGORY_POWER_UP;
        playerFixture.filter.maskBits = Box2dContactListener.MASK_POWER_UP;
        body.createFixture(playerFixture);
        body.setLinearDamping(10);
        body.setAngularDamping(2f);
        body.setUserData(this);

        circleShape.dispose();

       // TODO add a light?

    }

    public void render(SpriteBatch batch){
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.draw(batch);
    }

    @Override
    public CollidableType getType() {
        return CollidableType.POWER_UP;
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

    @Override
    public void collideWithWorld() {

    }

    @Override
    public void collisionDamage(float damage) {

    }

    @Override
    public float getVelocity() {
        return 0;
    }
}
