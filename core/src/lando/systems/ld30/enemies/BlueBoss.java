package lando.systems.ld30.enemies;

import com.badlogic.gdx.graphics.Color;
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
 * Created by dsgraham on 8/25/14.
 */
public class BlueBoss extends BlueEnemy {
    public BlueBoss(Vector2 position, GameScreen screen) {
        super(position, screen);

        speed = 40f;
        maxHitPoints = 100;
        hitPoints = 100;
        shieldAmount = 100;
        maxShield = 100;
        healthBar.setWidth(150);
        RELOAD_TIME = 4f;
        shieldBar.bounds.width = 100;
        healthBar.bounds.width = 200;
        healthBar.bounds.height = 20;
        healthBar.setBarColor(Color.BLUE.cpy());
        setHealthBarVerticalOffset(body_radius * 6);
        isBoss = true;
    }

    @Override
    public void update(float dt) {
        superUpdate(dt);

        // Always be chasing
        dir.set(screen.player.body.getPosition());
        dir.sub(body.getPosition()).nor().scl(speed);
        body.applyForceToCenter(dir.x, dir.y, true);

        if (reloadTimer <= 0){
            int bulletSpread = 4 + Assets.random.nextInt(4);
            float spreadOffset = Assets.random.nextFloat() * 360/bulletSpread;
            for (int i = 0; i < bulletSpread; i++){
                shootLaser(new Vector2(1, 0).rotate(spreadOffset + (i * (360 / bulletSpread))).add(body.getPosition()), Color.BLUE);
                reloadTimer = 5f;
            }
        }

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
        return new PowerUp(body.getPosition().cpy(), Color.BLUE);
    }

    @Override
    public void collideWithWorld() {
        return;
    }
}
