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
public class GreenEnemy extends Enemy {

    public GreenEnemy(Vector2 position, GameScreen screen) {
        super(position, screen);
        speed = 7.5f;
        healthBar.setBarColor(new Color(0, 0.7f, 0, 1));
    }

    @Override
    public void update(float dt) {
        super.update(dt);


        dir.set(screen.player.body.getPosition());
        dir.sub(body.getPosition()).nor().scl(speed);
        body.applyForceToCenter(dir.x, dir.y, true);

        hitPoints = Math.min(hitPoints + (dt * 2), maxHitPoints);


    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    @Override
    public CollidableType getType() {
        return CollidableType.GREEN_ENEMY;
    }

    @Override
    protected void intializeSprite() {
        animation = new Animation(0.1f,
                Assets.atlas.findRegion("green-enemy0"),
                Assets.atlas.findRegion("green-enemy1"),
                Assets.atlas.findRegion("green-enemy2"),
                Assets.atlas.findRegion("green-enemy3"),
                Assets.atlas.findRegion("green-enemy4"));
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
