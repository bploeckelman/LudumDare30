package lando.systems.ld30.enemies;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import lando.systems.ld30.HealthBar;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.tweens.Vector2Accessor;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Box2dContactListener;
import lando.systems.ld30.utils.Globals;

/**
 * Created by dsgraham on 8/25/14.
 */
public class FinalBoss extends Enemy{

    int nextAttackType = 0;

    public FinalBoss(Vector2 position, GameScreen screen) {
        super(position, screen);
        speed = 0f;
        healthBar.setBarColor(Color.PURPLE.cpy());

        maxHitPoints = 500;
        hitPoints = 500;
        shieldAmount = 80;
        maxShield = 80;
        healthBar.setWidth(150);
        RELOAD_TIME = 4f;
        healthBar.bounds.width = 200;
        healthBar.bounds.height = 20;
        healthBar.setBarColor(Color.BLUE.cpy());

        shieldBar = new HealthBar(40, 12, Color.LIGHT_GRAY.cpy(), new Color(0.6f, 0.8f, 1, 1));
        shieldBar.bounds.width = 100;
        setHealthBarVerticalOffset(body_radius * 6);
        isBoss = true;
    }

    public void update(float dt){
        super.update(dt);

        if (reloadTimer <= 0) {
            switch (nextAttackType) {
                case 0:

                        int bulletSpread = 4 + Assets.random.nextInt(10);
                        float spreadOffset = Assets.random.nextFloat() * 360/bulletSpread;
                        for (int i = 0; i < bulletSpread; i++){
                            shootLaser(new Vector2(1, 0).rotate(spreadOffset + (i * (360 / bulletSpread))).add(body.getPosition()), Color.BLUE);
                            reloadTimer = 5f;

                    }
                    break;
                case 1:
                    shootLaser(screen.player.body.getPosition(), Color.YELLOW);
                    bulletSpread = 10 + Assets.random.nextInt(15);
                    for (int i = 0; i < bulletSpread; i++){
                        new Vector2(1,0);
                        shootBulletDir(new Vector2(1, 0).rotate(i * (360 / bulletSpread)));

                    }
                    break;
                case 2:
                    shootLaser(screen.player.body.getPosition(), Color.GREEN);
                    bulletSpread = 20 + Assets.random.nextInt(15);

                    bulletSpeed = 800;
                    for (int i = 0; i < bulletSpread; i++){
                        new Vector2(1,0);

                        shootBulletDir(new Vector2(1, 0).rotate(i * (360 / bulletSpread)));

                    }

                    bulletSpread += 10;

                    bulletSpeed = 600;
                    for (int i = 0; i < bulletSpread; i++){
                        new Vector2(1,0);
                        shootBulletDir(new Vector2(1, 0).rotate(i * (360 / bulletSpread)));

                    }

                    break;
                case 3:
                    shootLaser(screen.player.body.getPosition(), Color.CYAN);
                    bulletSpread = 10 + Assets.random.nextInt(10);

                    for (int i = 0; i < bulletSpread; i++){
                        new Vector2(1,0);

                        shootSeekerDir(new Vector2(1, 0).rotate(i * (360 / bulletSpread)));

                    }

                    break;
                case 4:

                        bulletSpread = 10 + Assets.random.nextInt(4);
                    spreadOffset = Assets.random.nextFloat() * 360/bulletSpread;
                        for (int i = 0; i < bulletSpread; i++){
                            shootLaser(new Vector2(1, 0).rotate(spreadOffset + (i * (360 / bulletSpread))).add(body.getPosition()), Color.BLUE);
                            reloadTimer = 5f;
                        }

                    break;
                case 5:
                    bulletSpread = 8 + Assets.random.nextInt(4);
                    spreadOffset = Assets.random.nextFloat() * 360/bulletSpread;
                    for (int i = 0; i < bulletSpread; i++) {
                        Vector2 target = new Vector2(1, 0).rotate(spreadOffset + (i * (360 / bulletSpread)));
                        Vector2 nextTarget = new Vector2(1, 0).rotate(spreadOffset + ((i + 1) * (360 / bulletSpread)));
                        LASER_DAMAGE = 2f;
                        shootMovingLaser(target, Color.PURPLE);
                        Tween.to(target, Vector2Accessor.XY, RELOAD_TIME)
                                .target(nextTarget.x, nextTarget.y)
                                .ease(Linear.INOUT)
                                .start(Globals.game.tweenManager);

                    }
                    break;

            }
            reloadTimer = 4f;
            nextAttackType = Assets.random.nextInt(6);
            setColor();
        }
        //shootSeeker(screen.player.body.getPosition());

    }

    private void setColor(){
        Color color = Color.WHITE;
        switch (nextAttackType) {
            case 0:
                color =Color.RED;
                break;
            case 1:
                color =Color.YELLOW;
                break;
            case 2:
                color =Color.GREEN;
                break;
            case 3:
                color =Color.CYAN;
                break;
            case 4:
                color =Color.BLUE;
                break;
            case 5:
                color =Color.PURPLE;
                break;
        }

        sprite.setColor(color.cpy().mul(.5f, .5f, .5f, 1));
    }

    float shimmerRadius = 40f;
    public void render(SpriteBatch batch){
        if (alive){
            batch.setShader(Assets.shimmerProgram);
            Assets.shimmerProgram.setUniformf("u_time", animTimer);
            Assets.shimmerProgram.setUniformf("u_alpha", .3f);
            batch.draw(Assets.rainbow, body.getPosition().x - shimmerRadius, body.getPosition().y - shimmerRadius, 2* shimmerRadius,2 * shimmerRadius);
            batch.setShader(null);
        }

        super.render(batch);
    }

    @Override
    protected void intializeSprite() {
        //TODO make this something else

        animation = new Animation(0.75f,
                Assets.atlas.findRegion("player0"),
                Assets.atlas.findRegion("player1"),
                Assets.atlas.findRegion("player2"),
                Assets.atlas.findRegion("player3"));
        animation.setPlayMode(Animation.PlayMode.LOOP);
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
        Fixture.density = 100000;
        Fixture.filter.categoryBits = Box2dContactListener.CATEGORY_FINAL;
        Fixture.filter.maskBits = Box2dContactListener.MASK_FINAL;
        body.createFixture(Fixture);

        body.setLinearDamping(1f);
        body.setAngularDamping(2f);
        body.setUserData(this);

        circleShape.dispose();
    }

    @Override
    public boolean isShieldUp() {
        return true;
    }
}
