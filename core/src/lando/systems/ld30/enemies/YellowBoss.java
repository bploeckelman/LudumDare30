package lando.systems.ld30.enemies;

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
 * Created by dsgraham on 8/25/14.
 */
public class YellowBoss extends YellowEnemy {
    public YellowBoss(Vector2 position, GameScreen screen) {
        super(position, screen);

        maxHitPoints = 100;
        hitPoints = 100;
        healthBar.setWidth(150);
        isBoss = true;
    }

    public void update(float dt)
    {
        super.superUpdate(dt);

        // Always be chasing
        dir.set(screen.player.body.getPosition());
        dir.sub(body.getPosition()).nor().scl(speed);
        body.applyForceToCenter(dir.x, dir.y, true);

        if (reloadTimer <= 0){
            int bulletSpread = 10 + Assets.random.nextInt(15);
            for (int i = 0; i < bulletSpread; i++){
                new Vector2(1,0);
                shootBulletDir(new Vector2(1, 0).rotate(i * (360 / bulletSpread)));
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
        return new PowerUp(body.getPosition().cpy());
    }
}
