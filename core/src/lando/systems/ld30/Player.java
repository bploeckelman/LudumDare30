package lando.systems.ld30;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.tweens.PointLightAccessor;
import lando.systems.ld30.utils.*;

import java.util.ArrayList;

/**
 * Created by dsgraham on 8/23/14.
 */
public class Player implements InputProcessor, Collidable {

    private static final BodyDef bodyDef = new BodyDef();
    public static final float max_hit_points = 100;

    public Body body;
//    public float speed;
    public Animation animation;
    public Sprite sprite;
    public Sprite shieldSprite;
    private GameScreen screen;
    private LaserShot shot;
    public int currentColor;
    public ArrayList<Globals.COLORS> availableColors;
    public boolean alive;
    public float respawnTimer;
    public float animTimer;
    Fixture fixture;
    public PointLight playerLight;
    public float reloadTimer = 0;
    public float bulletSpeed = 800;
    public float seekerSpeed = 500;
    public float hitPoints = max_hit_points;
    public HealthBar healthBar;
    public HealthBar shieldBar;
    public float timeSinceLastHit = 0;
    public float shieldAmount = 50;
    public float maxShield = 50;

    public final static float RED_RELOAD_TIME = 2f;
    public final static float YELLOW_RELOAD_TIME = .2f;
    public final static float CYAN_RELOAD_TIME = .8f;
    public final static float SHIELD_DELAY = 10f;
    public final static float PURPLE_LASER_DAMAGE = .3f;



    public Player (Vector2 position, GameScreen screen){
        currentColor = 0;
        alive = true;
        availableColors = new ArrayList<Globals.COLORS>();
        this.screen = screen;
//        this.speed = 2000f;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(2f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        body = Globals.world.createBody(bodyDef);
        FixtureDef playerFixture;
        playerFixture = new FixtureDef();
        playerFixture.shape = circleShape;
        playerFixture.density = 1f;
        playerFixture.filter.categoryBits = Box2dContactListener.CATEGORY_PLAYER;
        playerFixture.filter.maskBits = Box2dContactListener.MASK_PLAYER;
        fixture = body.createFixture(playerFixture);


        body.setLinearDamping(4.0f);
        body.setAngularDamping(2f);
        body.setUserData(this);
        circleShape.dispose();

        animation = new Animation(0.075f,
                Assets.atlas.findRegion("player0"),
                Assets.atlas.findRegion("player1"),
                Assets.atlas.findRegion("player2"),
                Assets.atlas.findRegion("player3"));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animTimer = 0;

        sprite = new Sprite();

        sprite.setOriginCenter();
        sprite.setSize(4f,4f);

        shieldSprite = new Sprite(Assets.circle);
        shieldSprite.setSize(5,5);
        shieldSprite.setOriginCenter();
        shieldSprite.setColor(Globals.shieldColor.cpy());

        playerLight = new PointLight(screen.rayHandler, screen.num_rays);
        playerLight.setColor(0,0,0,1);
        playerLight.setDistance(0);
        playerLight.attachToBody(body, 0, 0);

        healthBar = new HealthBar(100, 18);
        shieldBar = new HealthBar(80, 12, Color.LIGHT_GRAY.cpy(), Globals.shieldColor.cpy());

        //TODO DEBUG STUFF
//        availableColors.add(Globals.COLORS.YELLOW);
//        availableColors.add(Globals.COLORS.RED);
//        availableColors.add(Globals.COLORS.GREEN);
//        availableColors.add(Globals.COLORS.BLUE);
//        availableColors.add(Globals.COLORS.CYAN);
//        availableColors.add(Globals.COLORS.PURPLE);
    }

    private final float MAX_VELOCITY = 30f;
    private final float speed = 2000;//30;
    public void update(float dt) {
        if (respawnTimer > 0 ){
            if (alive) {
                alive = false;
                body.destroyFixture(fixture);
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(2f);
                FixtureDef deadFixtureDef = new FixtureDef();
                deadFixtureDef.shape = circleShape;
                deadFixtureDef.density = 1f;
                deadFixtureDef.filter.categoryBits = Box2dContactListener.CATEGORY_PLAYER;
                deadFixtureDef.filter.maskBits = Box2dContactListener.MASK_DEAD;
                fixture = body.createFixture(deadFixtureDef);
                circleShape.dispose();
            }
            respawnTimer -= dt;
            if (respawnTimer <= 0){
                respawnTimer = 0;
                shieldAmount = maxShield;
                hitPoints = max_hit_points;
                healthBar.setValue(1);
                body.setTransform(1000, 1000-10, 0);
                screen.enterLevel(GameScreen.LEVEL_STATE.OVER_MAP, true);
                alive = true;
                body.destroyFixture(fixture);
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(2f);
                FixtureDef aliveFixtureDef;
                aliveFixtureDef = new FixtureDef();
                aliveFixtureDef.shape = circleShape;
                aliveFixtureDef.density = 1f;
                aliveFixtureDef.filter.categoryBits = Box2dContactListener.CATEGORY_PLAYER;
                aliveFixtureDef.filter.maskBits = Box2dContactListener.MASK_PLAYER;
                fixture = body.createFixture(aliveFixtureDef);
                circleShape.dispose();
            }
        }
        healthBar.setValue(getPercentHP());
        shieldBar.setValue(getPercentShield());

        if (!Assets.playerDeathParticleEffect.isComplete()) {
            Assets.playerDeathParticleEffect.update(dt);
        }

        timeSinceLastHit += dt;
        reloadTimer -= dt;
        reloadTimer = Math.max(reloadTimer, 0);

        if (shot != null) {
            shot.update(dt);
            if (!shot.alive) shot = null;
        }

        fire(dt);


        Vector2 vel = body.getLinearVelocity();
        if (Gdx.input.isKeyPressed(Input.Keys.W) && vel.y < MAX_VELOCITY){
//            body.applyForceToCenter(0, speed, true);
            body.applyLinearImpulse(0, speed * dt, body.getPosition().x, body.getPosition().y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && vel.y > -MAX_VELOCITY){
//            body.applyForceToCenter(0, -speed * dt, true);
            body.applyLinearImpulse(0, -speed * dt, body.getPosition().x, body.getPosition().y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && vel.x > -MAX_VELOCITY){
//            body.applyForceToCenter(-speed * dt, 0, true);
            body.applyLinearImpulse(-speed * dt, 0, body.getPosition().x, body.getPosition().y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && vel.x < MAX_VELOCITY){
//            body.applyForceToCenter(speed * dt, 0, true);
            body.applyLinearImpulse(speed * dt, 0, body.getPosition().x, body.getPosition().y, true);
        }

        vel = body.getLinearVelocity();
        if (vel.x >  MAX_VELOCITY) vel.x =  MAX_VELOCITY;
        if (vel.x < -MAX_VELOCITY) vel.x = -MAX_VELOCITY;
        if (vel.y >  MAX_VELOCITY) vel.y =  MAX_VELOCITY;
        if (vel.y < -MAX_VELOCITY) vel.y = -MAX_VELOCITY;
        body.setLinearVelocity(vel);

        shieldSprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));

        animTimer += dt;
        sprite.setRegion(animation.getKeyFrame(animTimer));
    }

    public void fire(float dt){
        if (availableColors.size() <= 0 ) return;
        if (Gdx.input.isTouched() && alive && reloadTimer <= 0 ){  //TODO: make this not shot if you have no colors

            Vector2 target = screen.getPosFromScreen(Gdx.input.getX(), Gdx.input.getY());
            switch (availableColors.get(currentColor)){
                case RED:
                    shootLaser(target, Color.RED);
                    Assets.redLaserSound.play(0.1f);
                    reloadTimer = RED_RELOAD_TIME;
                    break;
                case YELLOW:
                    reloadTimer = YELLOW_RELOAD_TIME;
                    shootBullet(target);
                    Assets.yellowBulletSound.play(0.1f);
                    break;
                case CYAN:
                    reloadTimer = CYAN_RELOAD_TIME;
                    shootSeeker(target);
                    Assets.homingBulletSound.play(0.1f);
                    break;
                case PURPLE:
                    reloadTimer = 5f;
                    purpleTarget = target.cpy();
                    shootPurpleLaser();
                    Assets.purpleLaserSound.play(0.1f);

                    break;
            }

        }

        if (availableColors.get(currentColor) == Globals.COLORS.PURPLE){
            if (Gdx.input.isTouched() && alive) {
                purpleTarget.set(screen.getPosFromScreen(Gdx.input.getX(), Gdx.input.getY()));
            }  else {
               if (shot != null){
                   //TODO kill light?
                   Assets.purpleLaserSound.stop();
                   reloadTimer = (RED_RELOAD_TIME- shot.timeLeft);
               }
               shot = null;
            }
        }

        if (availableColors.get(currentColor) == Globals.COLORS.GREEN){
            hitPoints = Math.min(hitPoints + (dt * 10), 100);
        }
        if (availableColors.get(currentColor) == Globals.COLORS.BLUE && timeSinceLastHit > SHIELD_DELAY){
            shieldAmount = Math.min(shieldAmount + (dt * 10), maxShield);
        }
    }

    public void render(SpriteBatch batch){
        if (shot != null) shot.render(batch);

        if (availableColors.size() > 0){
            sprite.setColor(getColor());
        } else {
            sprite.setColor(Color.WHITE);
        }
        if (alive) {// || respawnTimer % .4f > .2f){
            shieldSprite.setColor(Globals.shieldColor.cpy().mul(shieldAmount/maxShield));
            if (isShieldUp())
                shieldSprite.draw(batch);
            sprite.draw(batch);

        }

        if (!Assets.playerDeathParticleEffect.isComplete()) {
            Assets.playerDeathParticleEffect.draw(batch);
        }
    }

    public Color getColor(){
        switch (availableColors.get(currentColor)){
            case RED:
                return Color.RED;
            case YELLOW:
                return Color.YELLOW;
            case GREEN:
                return Color.GREEN;
            case CYAN:
                return Color.CYAN;
            case BLUE:
                return Color.BLUE;
            case PURPLE:
                return Color.PURPLE;

        }
        return Color.WHITE;
    }

    public int getColorIndex() {
        if (availableColors.isEmpty()) return -1;
        switch (availableColors.get(currentColor)){
            case RED:     return 0;
            case YELLOW:  return 1;
            case GREEN:   return 2;
            case CYAN:    return 3;
            case BLUE:    return 4;
            case PURPLE:  return 5;
            default:      return -1;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        //screen.camera.zoom += amount;

        int colors = availableColors.size();
        if (colors > 0){
            if (amount > 0){
                currentColor++;
            } else {
                currentColor--;
            }
            if (currentColor >= colors){
                currentColor = 0;
            }
            if (currentColor < 0){
                currentColor = colors -1;
            }
        }
        shot = null;
        timeSinceLastHit = Math.min(timeSinceLastHit, SHIELD_DELAY/2f);
        return false;
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
        hitPoints -= amount;
        Assets.playerTakeDamageSound.play(0.1f);

        if (hitPoints <= 0) {
            hitPoints = 0;
            kill();
        }
    }

    public void kill(){
        if (respawnTimer <= 0){
            respawnTimer = 3f;
            shot = null;
            screen.game.tweenManager.killTarget(playerLight);
            playerLight.setColor(0, 0, 0, 1);
            playerLight.setDistance(0);
            Assets.playerDeathParticleEffect.reset();
            Assets.playerDeathParticleEffect.setPosition(body.getPosition().x, body.getPosition().y);
            Stats.playerDeaths++;
            Assets.playerDeathSound.play(0.1f);
        }
    }

    public void shootBullet(Vector2 target){
        Vector2 dir = target.cpy().sub(body.getPosition());
        screen.bullets.add(new Bullet(body.getPosition().cpy(), dir, Color.WHITE, true, bulletSpeed, 5f));
    }

    public void shootSeeker(Vector2 target){
        Vector2 dir = target.cpy().sub(body.getPosition());
        screen.bullets.add(new SeekingBullet(body.getPosition().cpy(), dir, Color.CYAN, true, seekerSpeed, 8f, 10f));
    }

    private void shootLaser(Vector2 target, Color color) {
        shot = new LaserShot(body, target, color, RED_RELOAD_TIME, 10);
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(playerLight, PointLightAccessor.DIST, RED_RELOAD_TIME).target(10))
                .push(Tween.to(playerLight, PointLightAccessor.RGB, RED_RELOAD_TIME).target(color.r, color.g, color.b).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        playerLight.setColor(0,0,0,1);
                    }
                }))
                .end()
                .push(Tween.to(playerLight, PointLightAccessor.DIST, 0.1f).target(0))
                .start(screen.game.tweenManager);
    }

    private Vector2 purpleTarget;
    private void shootPurpleLaser(){
        Color color = Color.PURPLE;
        shot = new LaserShot(body, purpleTarget, color, RED_RELOAD_TIME, PURPLE_LASER_DAMAGE);
        shot.alwaysOn = true;
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(playerLight, PointLightAccessor.DIST, RED_RELOAD_TIME).target(10))
                .push(Tween.to(playerLight, PointLightAccessor.RGB, RED_RELOAD_TIME).target(color.r, color.g, color.b).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        playerLight.setColor(0,0,0,1);
                    }
                }))
                .end()
                .push(Tween.to(playerLight, PointLightAccessor.DIST, 0.1f).target(0))
                .start(screen.game.tweenManager);
    }

    @Override
    public CollidableType getType() {
        return CollidableType.PLAYER;
    }

    @Override
    public void shotByPlayer(LaserShot laser) {

    }

    @Override
    public void shotByEnemy(LaserShot laser) {
        takeDamage(laser.damage);
    }

    @Override
    public boolean collideWithBullet(Bullet bullet) {
        if (alive) {
            takeDamage(bullet.damage);
            return true;
        }
        return false;
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
        return hitPoints / max_hit_points;
    }

    public float getPercentShield() {
        if (!alive) return 0;
        // return shieldPoints / max_shield_points;
        return shieldAmount/maxShield;
    }

    public boolean isShieldUp() {
        // TODO : return shield_up_flag;
        return availableColors.size() != 0 && availableColors.get(currentColor) == Globals.COLORS.BLUE;
    }
}
