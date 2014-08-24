package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Collidable;
import lando.systems.ld30.utils.CollidableType;
import lando.systems.ld30.utils.Globals;

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

    public Bullet(Vector2 position, Vector2 dir, Color color){
        this.color = color;
        alive = true;
        TTL = 30f;
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(body_radius);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = Globals.world.createBody(bodyDef);
        body.createFixture(circleShape, 10f);
        body.setLinearDamping(0);
        body.setAngularDamping(2f);
        body.setUserData(this);
        body.applyLinearImpulse(dir, new Vector2(0,0), true);
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
        Globals.world.destroyBody(body);
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
    public void shotByEnemy(Color color) {

    }
}
