package lando.systems.ld30;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
 * Brian Ploeckelman created on 8/23/2014.
 */
public class Enemy implements Collidable {

    private static final BodyDef bodyDef = new BodyDef();

    public float body_radius;

    public GameScreen screen;

    public Body body;
    public Sprite sprite;
    public Animation animation;
    public TextureRegion keyframe;

    public float speed;
    public float animTimer;

    public Enemy(Vector2 position, GameScreen screen) {
        this.screen = screen;

        body_radius = 1.5f;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(body_radius);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = Globals.world.createBody(bodyDef);
        body.createFixture(circleShape, 1f);
        body.setLinearDamping(1f);
        body.setAngularDamping(2f);
        body.setUserData(this);

        circleShape.dispose();

        animation = new Animation(0.075f,
                Assets.atlas.findRegion("red-enemy0"),
                Assets.atlas.findRegion("red-enemy1"),
                Assets.atlas.findRegion("red-enemy2"),
                Assets.atlas.findRegion("red-enemy3"),
                Assets.atlas.findRegion("red-enemy4"),
                Assets.atlas.findRegion("red-enemy5"));
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        sprite = new Sprite();
        sprite.setOriginCenter();
        sprite.setSize(2*body_radius, 2*body_radius);

        speed = .1f;
        animTimer = 0;
    }

    public void update(float dt) {
        animTimer += dt;
        keyframe = animation.getKeyFrame(animTimer);

        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));

        sprite.setTexture(keyframe.getTexture());
        sprite.setRegion(keyframe.getU(), keyframe.getV(), keyframe.getU2(), keyframe.getV2());
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public CollidableType getType() {
        return CollidableType.ENEMY;
    }
}
