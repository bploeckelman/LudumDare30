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
import lando.systems.ld30.utils.*;

import java.util.ArrayList;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public abstract class Enemy implements Collidable {

    protected static final BodyDef bodyDef = new BodyDef();

    public ArrayList<EnemyLaserShot> lasers = new ArrayList<EnemyLaserShot>();
    public PointLight enemyLight;

    public float body_radius;

    public GameScreen screen;

    public Body body;
    public Sprite sprite;
    public Sprite shieldSprite;
    public Animation animation;
    public HealthBar healthBar;
    public HealthBar shieldBar;

    public float speed;
    public float animTimer;
    public float bulletSpeed = 800;
    public float seekerSpeed = 500;
    public float SEEKER_TIME = 4f;

    public boolean isBoss;
    public boolean alive;

    public float hitPoints = 10f;
    public float maxHitPoints = 10f;
    public float shieldAmount = 10;
    public float maxShield = 10;
    public float timeSinceLastHit =0;

    public float RELOAD_TIME = 3f;
    public float LASER_DAMAGE = 10f;
    public float BULLET_DAMAGE = 5f;
    public float SEEKER_DAMAGE = 20;
    public float SHIELD_DELAY = 10f;
    public float reloadTimer = 0;
    public MutableFloat rotation = new MutableFloat(0);

    // Temporary helper vectors for calculating all the things
    protected Vector2 dist = new Vector2();
    protected Vector2 dir = new Vector2();

    public Enemy(Vector2 position, GameScreen screen) {
        this.screen = screen;
        alive = true;
        isBoss = false;

        initializeBox2dBody(position);
        intializeSprite();

        shieldSprite = new Sprite(Assets.circle);
        shieldSprite.setSize(5,5);
        shieldSprite.setOriginCenter();
        shieldSprite.setColor(Globals.shieldColor.cpy());

        sprite.setRegion(animation.getKeyFrame(animTimer));
        enemyLight = new PointLight(screen.rayHandler, screen.num_rays);
        enemyLight.setColor(0,0,0,1);
        enemyLight.setDistance(0);
        enemyLight.attachToBody(body, 0, 0);
        reloadTimer = RELOAD_TIME;

        healthBar = new HealthBar(50, 12, Color.LIGHT_GRAY, Globals.shieldColor.cpy());
        shieldBar = null;
        setHealthBarVerticalOffset(body_radius * 6);
    }

    public void update(float dt) {
        reloadTimer = Math.max(reloadTimer - dt, 0);
        animTimer += dt;
        timeSinceLastHit += dt;
        sprite.setRegion(animation.getKeyFrame(animTimer));
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));

        healthBar.setValue(getPercentHP());
        if (shieldBar != null) shieldBar.setValue(getPercentShield());

        for (int i = lasers.size(); --i >= 0;) {
            EnemyLaserShot shot = lasers.get(i);
            if (shot != null) {
                shot.update(dt);
                if (!shot.alive) lasers.remove(i);
            }
        }
        if (timeSinceLastHit > SHIELD_DELAY){
            shieldAmount = Math.min(shieldAmount + (dt * 10), maxShield);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < lasers.size(); i++) {
            lasers.get(i).render(batch);
        }
        sprite.draw(batch);
        shieldSprite.setColor(Globals.shieldColor.cpy().mul(shieldAmount / maxShield));
        if (isShieldUp())
            shieldSprite.draw(batch);
    }

    public void takeDamage (float amount){
        timeSinceLastHit = 0;
        if (isShieldUp()){
            if (shieldAmount > amount){
                shieldAmount -= amount;
                amount = 0;
            } else {
                amount -= shieldAmount;
                shieldAmount = 0;
            }
        }
        Assets.enemyDamageSound.play(0.1f);

        hitPoints -= amount;
        if (hitPoints <= 0) {
            kill();
        }
    }

    public void kill(){
        alive = false;
        screen.game.tweenManager.killTarget(enemyLight);
        lasers.clear();
        enemyLight.setColor(0, 0, 0, 1);
        enemyLight.setDistance(0);

        if (this instanceof RedEnemy)    Stats.redEnemiesKilled++;
        if (this instanceof YellowEnemy) Stats.yellowEnemiesKilled++;
        if (this instanceof GreenEnemy)  Stats.greenEnemiesKilled++;
        if (this instanceof CyanEnemy)   Stats.cyanEnemiesKilled++;
        if (this instanceof BlueEnemy)   Stats.blueEnemiesKilled++;
        if (this instanceof PurpleEnemy) Stats.purpleEnemiesKilled++;

        if (this instanceof RedBoss
         || this instanceof YellowBoss
         || this instanceof GreenBoss
         || this instanceof CyanBoss
         || this instanceof BlueBoss
         || this instanceof PurpleBoss
         || this instanceof FinalBoss) {
            Assets.playerDeathSound.play(0.2f);
        } else {
            Assets.enemyDeathSound.play(0.1f);
        }
    }

    public void shootBullet(Vector2 target){
        reloadTimer = RELOAD_TIME;
        Vector2 dir = target.cpy().sub(body.getPosition());
        screen.bullets.add(new Bullet(body.getPosition().cpy(), dir, Color.WHITE, false, bulletSpeed, BULLET_DAMAGE));
//        Assets.yellowBulletSound.play(0.05f);
    }

    public void shootBulletDir(Vector2 dir){
        screen.bullets.add(new Bullet(body.getPosition().cpy(), dir, Color.WHITE, false, bulletSpeed, BULLET_DAMAGE));

    }

    public void shootSeeker(Vector2 target){
        reloadTimer = RELOAD_TIME;
        Vector2 dir = target.cpy().sub(body.getPosition());
        screen.bullets.add(new SeekingBullet(body.getPosition().cpy(), dir, Color.WHITE, false, seekerSpeed, SEEKER_DAMAGE, SEEKER_TIME));
//        Assets.homingBulletSound.play(0.05f);
    }

    public void shootSeekerDir(Vector2 dir){
        screen.bullets.add(new SeekingBullet(body.getPosition().cpy(), dir, Color.WHITE, false, seekerSpeed, SEEKER_DAMAGE, SEEKER_TIME));

    }

    protected void shootLaser(Vector2 target, Color color) {
        reloadTimer = RELOAD_TIME;
        lasers.add(new EnemyLaserShot(body, target, color, RELOAD_TIME, LASER_DAMAGE));
        enemyLight.setActive(true);
//        Assets.redLaserSound.play(0.05f);
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(enemyLight, PointLightAccessor.DIST, RELOAD_TIME).target(8))
                .push(Tween.to(enemyLight, PointLightAccessor.RGB, RELOAD_TIME).target(color.r, color.g, color.b).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        enemyLight.setColor(0,0,0,1);
                        enemyLight.setActive(false);
                    }
                }))
                .end()
                .push(Tween.to(enemyLight, PointLightAccessor.DIST, 0.1f).target(0))
                .start(screen.game.tweenManager);
    }

    protected void shootMovingLaser(Vector2 target, Color color){
        reloadTimer = RELOAD_TIME;
        lasers.add(new EnemyMovingLaserShot(body, target, color, RELOAD_TIME, LASER_DAMAGE));
        enemyLight.setActive(true);
//        Assets.purpleLaserSound.play(0.05f);
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(enemyLight, PointLightAccessor.DIST, RELOAD_TIME).target(8))
                .push(Tween.to(enemyLight, PointLightAccessor.RGB, RELOAD_TIME).target(color.r, color.g, color.b).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        enemyLight.setColor(0,0,0,1);
                        enemyLight.setActive(false);
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
    public void shotByPlayer(LaserShot laser) {
         takeDamage(laser.damage);
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
        if (isShieldUp() && shieldAmount > 0) damage/= 10f;
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

    public float getPercentShield() {
        if (!alive) return 0;
        // return shieldPoints / max_shield_points;
        return shieldAmount/maxShield;
    }

    public boolean isShieldUp() {
        return false;
    }

    protected abstract void intializeSprite();
    protected abstract void initializeBox2dBody(Vector2 position);

    public PowerUp getPowerUP() {
        return null;
    }

    protected void setHealthBarVerticalOffset(float offset){
        healthBar.verticalOffset = offset;
        if (shieldBar != null) {
            shieldBar.verticalOffset = offset;
        }
    }
}
