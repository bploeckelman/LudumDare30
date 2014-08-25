package lando.systems.ld30.enemies;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import lando.systems.ld30.PowerUp;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Box2dContactListener;
import lando.systems.ld30.utils.Globals;

/**
 * Created by dsgraham on 8/24/14.
 */
public class RedBoss extends RedEnemy {

    boolean chargeNext = false;
    float chargeTimer = 0;

    Vector2 chargeDir;

    public RedBoss(Vector2 position, GameScreen screen) {
        super(position, screen);
        speed = 20f;
        maxHitPoints = 100;
        hitPoints = 100;
        healthBar.setWidth(150);
        RELOAD_TIME = 4f;
        healthBar.bounds.width = 200;
        healthBar.bounds.height = 20;
        healthBar.setBarColor(Color.RED.cpy());
        isBoss = true;
        // TODO: Play Sound on spawn
    }

    public void update(float dt){
        superUpdate(dt);

        body.setTransform(body.getPosition(), rotation.floatValue());

        dist.set(screen.player.body.getPosition());
        final float d2 = dist.dst2(body.getPosition());
        final float shoot_dist2 = 1200;
        final float pursueDist2 = 22500;
        if (chargeTimer > 0){
            chargeTimer -= dt;
            if (chargeTimer <= 0)
            {
                chargeTimer = 0;
                reloadTimer = RELOAD_TIME;
                rotation.setValue(0);
                body.applyLinearImpulse(chargeDir.nor().scl(4000), new Vector2(), true);
            }
        }
        else if (d2 < shoot_dist2) {
            if (reloadTimer <= 0) {
                if (chargeNext){
                    reloadTimer = 99f;
                    chargeTimer = 2f;
                    chargeDir = screen.player.body.getPosition().cpy().sub(body.getPosition());
                    Tween.to(rotation, 0, 2f)
                            .target(1280)
                            .ease(Linear.INOUT)
                            .setCallbackTriggers(TweenCallback.END)
                            .start(Globals.game.tweenManager);
                } else {
                    shootLaser(screen.player.body.getPosition().cpy(), Color.RED);
                    reloadTimer = RELOAD_TIME * 2;
                }
                chargeNext = !chargeNext;
            }
        } else  if (d2 < pursueDist2){
            dir.set(screen.player.body.getPosition());
            dir.sub(body.getPosition()).nor().scl(speed);
            body.applyForceToCenter(dir.x, dir.y, true);
        }
    }

    @Override
    protected void intializeSprite() {
        animation = new Animation(0.075f,
                Assets.atlas.findRegion("red-enemy0"),
                Assets.atlas.findRegion("red-enemy1"),
                Assets.atlas.findRegion("red-enemy2"),
                Assets.atlas.findRegion("red-enemy3"),
                Assets.atlas.findRegion("red-enemy4"),
                Assets.atlas.findRegion("red-enemy5"));
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        animTimer = 0;

        sprite = new Sprite();
        sprite.setOriginCenter();
        sprite.setSize(2*body_radius, 2*body_radius);
    }

    @Override
    protected void initializeBox2dBody(Vector2 position) {
        body_radius = 10f;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(body_radius);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = Globals.world.createBody(bodyDef);
        FixtureDef Fixture = new FixtureDef();
        Fixture.shape = circleShape;
        Fixture.density = .01f;
        Fixture.filter.categoryBits = Box2dContactListener.CATEGORY_ENEMY;
        Fixture.filter.maskBits = Box2dContactListener.MASK_ENEMY;
        body.createFixture(Fixture);

        body.setLinearDamping(1f);
        body.setAngularDamping(2f);
        body.setUserData(this);

        circleShape.dispose();
    }

    public PowerUp getPowerUP() {
        return new PowerUp(body.getPosition().cpy());
    }
}
