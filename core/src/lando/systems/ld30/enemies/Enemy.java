package lando.systems.ld30.enemies;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import lando.systems.ld30.*;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.tweens.PointLightAccessor;
import lando.systems.ld30.utils.Collidable;
import lando.systems.ld30.utils.CollidableType;
import lando.systems.ld30.utils.Globals;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public abstract class Enemy implements Collidable {

    protected static final BodyDef bodyDef = new BodyDef();

    public EnemyLaserShot shot;
    public PointLight enemyLight;

    public float body_radius;

    public GameScreen screen;

    public Body body;
    public Sprite sprite;
    public Animation animation;
    public HealthBar healthBar;

    public float speed;
    public float animTimer;
    public float bulletSpeed = 800;
    public float seekerSpeed = 500;


    public boolean alive;

    public float hitPoints = 1f;
    public float maxHitPoints = 1f;
    public float RELOAD_TIME = 3f;
    public float LASER_DAMAGE = 10f;
    public float BULLET_DAMAGE = 5f;
    public float SEEKER_DAMAGE = 20;
    public float reloadTimer = 0;
    public MutableFloat rotation = new MutableFloat(0);

    // Temporary helper vectors for calculating all the things
    protected Vector2 dist = new Vector2();
    protected Vector2 dir = new Vector2();

    public Enemy(Vector2 position, GameScreen screen) {
        this.screen = screen;
        alive = true;

        initializeBox2dBody(position);
        intializeSprite();

        sprite.setRegion(animation.getKeyFrame(animTimer));
        enemyLight = new PointLight(screen.rayHandler, screen.num_rays);
        enemyLight.setColor(0,0,0,1);
        enemyLight.setDistance(0);
        enemyLight.attachToBody(body, 0, 0);
        reloadTimer = RELOAD_TIME;

        healthBar = new HealthBar(50, 25);
        healthBar.setValue(1);
    }

    public void update(float dt) {
        reloadTimer = Math.max(reloadTimer - dt, 0);
        animTimer += dt;
        sprite.setRegion(animation.getKeyFrame(animTimer));
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));

        healthBar.setValue(getPercentHP());

        if (shot != null) {
            shot.update(dt);
            if (!shot.alive) shot = null;
        }
    }

    public void render(SpriteBatch batch) {
        if (shot != null) shot.render(batch);
        sprite.draw(batch);
    }

    public void takeDamage (float amount){
        hitPoints -= amount;
        if (hitPoints <= 0) kill();
    }

    public void kill(){
        alive = false;
        screen.game.tweenManager.killTarget(enemyLight);
        shot = null;
        enemyLight.setColor(0, 0, 0, 1);
        enemyLight.setDistance(0);
    }

    public void shootBullet(Vector2 target){
        reloadTimer = RELOAD_TIME;
        Vector2 dir = target.cpy().sub(body.getPosition());
        screen.bullets.add(new Bullet(body.getPosition().cpy(), dir, Color.WHITE, false, bulletSpeed, BULLET_DAMAGE));
    }

    public void shootSeeker(Vector2 target){
        reloadTimer = RELOAD_TIME;
        Vector2 dir = target.cpy().sub(body.getPosition());
        screen.bullets.add(new SeekingBullet(body.getPosition().cpy(), dir, Color.WHITE, false, seekerSpeed, SEEKER_DAMAGE));
    }

    protected void shootLaser(Vector2 target, Color color) {
        reloadTimer = RELOAD_TIME;
        shot = new EnemyLaserShot(body, target, color, RELOAD_TIME, LASER_DAMAGE);
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(enemyLight, PointLightAccessor.DIST, RELOAD_TIME).target(8))
                .push(Tween.to(enemyLight, PointLightAccessor.RGB, RELOAD_TIME).target(color.r, color.g, color.b).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        enemyLight.setColor(0,0,0,1);
                    }
                }))
                .end()
                .push(Tween.to(enemyLight, PointLightAccessor.DIST, 0.1f).target(0))
                .start(screen.game.tweenManager);
    }

    @Override
    public CollidableType getType() {
        return CollidableType.ENEMY;
    }

    @Override
    public void shotByPlayer(Color color) {
         kill();
    }

    @Override
    public void shotByEnemy(LaserShot laser) {

    }

    @Override
    public boolean collideWithBullet(Bullet bullet) {
        takeDamage(bullet.damage);
        return true;
    }

    @Override
    public void collideWithWorld() {
        takeDamage(body.getLinearVelocity().len());
    }

    @Override
    public void collisionDamage(float damage) {
        takeDamage(damage);
    }

    @Override
    public float getVelocity() {
        return body.getLinearVelocity().len();
    }

    public float getPercentHP() {
        if (!alive) return 0;
        return hitPoints / maxHitPoints;
    }

    protected abstract void intializeSprite();
    protected abstract void initializeBox2dBody(Vector2 position);

}
