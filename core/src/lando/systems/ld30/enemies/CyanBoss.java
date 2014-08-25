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
public class CyanBoss extends CyanEnemy{
    public CyanBoss(Vector2 position, GameScreen screen) {
        super(position, screen);


        speed = 20f;
        maxHitPoints = 100;
        hitPoints = 100;
        healthBar.setWidth(150);
        RELOAD_TIME = 4f;
        healthBar.bounds.width = 200;
        healthBar.bounds.height = 20;
        healthBar.setBarColor(Color.CYAN.cpy());
        setHealthBarVerticalOffset(body_radius * 6);
        SEEKER_DAMAGE = 10f;
        SEEKER_TIME = 15f;
        isBoss = true;
    }

    public void update(float dt)
    {
        superUpdate(dt);

        dir.set(screen.player.body.getPosition());
        dir.sub(body.getPosition()).nor().scl(speed);
        body.applyForceToCenter(dir.x, dir.y, true);

        if (reloadTimer <= 0){
            shootLaser(screen.player.body.getPosition(), Color.CYAN);
            int bulletSpread = 5 + Assets.random.nextInt(10);

            for (int i = 0; i < bulletSpread; i++){
                new Vector2(1,0);

                shootSeekerDir(new Vector2(1, 0).rotate(i * (360 / bulletSpread)));

            }

            reloadTimer = 5f;
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
        return new PowerUp(body.getPosition().cpy(), Color.CYAN);
    }

    @Override
    public void collideWithWorld() {
        return;
    }
}
