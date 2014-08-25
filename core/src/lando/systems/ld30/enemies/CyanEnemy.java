package lando.systems.ld30.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Box2dContactListener;
import lando.systems.ld30.utils.CollidableType;
import lando.systems.ld30.utils.Globals;

/**
 * Brian Ploeckelman created on 8/24/2014.
 */
public class CyanEnemy extends Enemy {

    public CyanEnemy(Vector2 position, GameScreen screen) {
        super(position, screen);
        speed = 10f;
        healthBar.setBarColor(Color.CYAN.cpy());
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        dist.set(screen.player.body.getPosition());
        final float d2 = dist.dst2(body.getPosition());
        final float shoot_dist2 = 3600;
        if (d2 < shoot_dist2) {
            if (reloadTimer <= 0) {
                shootSeeker(screen.player.body.getPosition());
            }
        } else {
            dir.set(screen.player.body.getPosition());
            dir.sub(body.getPosition()).nor().scl(speed);
            body.applyForceToCenter(dir.x, dir.y, true);
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    @Override
    public CollidableType getType() {
        return CollidableType.CYAN_ENEMY;
    }

    @Override
    protected void intializeSprite() {
        animation = new Animation(0.075f,
                Assets.atlas.findRegion("cyan-enemy0"),
                Assets.atlas.findRegion("cyan-enemy0"),
                Assets.atlas.findRegion("cyan-enemy0"),
                Assets.atlas.findRegion("cyan-enemy1"),
                Assets.atlas.findRegion("cyan-enemy2"),
                Assets.atlas.findRegion("cyan-enemy3"),
                Assets.atlas.findRegion("cyan-enemy4"));
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        animTimer = 0;

        sprite = new Sprite();
        sprite.setOriginCenter();
        sprite.setSize(2*body_radius, 2*body_radius);
    }

    @Override
    protected void initializeBox2dBody(Vector2 position) {
        body_radius = 1.5f;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(body_radius);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = Globals.world.createBody(bodyDef);
        FixtureDef Fixture = new FixtureDef();
        Fixture.shape = circleShape;
        Fixture.density = .1f;
        Fixture.filter.categoryBits = Box2dContactListener.CATEGORY_ENEMY;
        Fixture.filter.maskBits = Box2dContactListener.MASK_ENEMY;
        body.createFixture(Fixture);

        body.setLinearDamping(1f);
        body.setAngularDamping(2f);
        body.setUserData(this);

        circleShape.dispose();
    }
}
