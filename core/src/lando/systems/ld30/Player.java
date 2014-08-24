package lando.systems.ld30;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.*;
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

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by dsgraham on 8/23/14.
 */
public class Player implements InputProcessor, Collidable {

    private static final BodyDef bodyDef = new BodyDef();

    public Body body;
    public float speed;
    public Animation animation;
    public Sprite sprite;
    private GameScreen screen;
    private LaserShot shot;
    public int currentColor;
    public ArrayList<Color> availableColors;
    public boolean alive;
    public float respawnTimer;
    public float animTimer;
    Fixture fixture;
    public PointLight playerLight;


    public Player (Vector2 position, GameScreen screen){
        currentColor = 0;
        alive = true;
        availableColors = new ArrayList<Color>();
        this.screen = screen;
        this.speed = 100f;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(2f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        body = Globals.world.createBody(bodyDef);
        //body.createFixture(circleShape, 1f);
        FixtureDef playerFixture;
        playerFixture = new FixtureDef();
        playerFixture.shape = circleShape;
        playerFixture.density = 1f;
        playerFixture.filter.categoryBits = Box2dContactListener.CATEGORY_PLAYER;
        playerFixture.filter.maskBits = Box2dContactListener.MASK_PLAYER;
        fixture = body.createFixture(playerFixture);


        body.setLinearDamping(1f);
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

        playerLight = new PointLight(screen.rayHandler, screen.num_rays);
        playerLight.setColor(0,0,0,1);
        playerLight.setDistance(0);
        playerLight.attachToBody(body, 0, 0);
    }

    private final float MAX_VELOCITY = 20f;
    public void update(float dt) {
        if (respawnTimer > 0 ){
            if (alive == true) {
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

        if (shot != null) {
            shot.update(dt);
            if (!shot.alive) shot = null;
        }

        Vector2 vel = body.getLinearVelocity();
        if (Gdx.input.isKeyPressed(Input.Keys.W) && vel.y < MAX_VELOCITY){
            body.applyForceToCenter(0, speed, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && vel.y > -MAX_VELOCITY){
            body.applyForceToCenter(0, -speed, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && vel.x > -MAX_VELOCITY){
            body.applyForceToCenter(-speed, 0, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && vel.x < MAX_VELOCITY){
            body.applyForceToCenter(speed, 0, true);
        }

        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));

        animTimer += dt;
        sprite.setRegion(animation.getKeyFrame(animTimer));
    }

    public void render(SpriteBatch batch){
        if (availableColors.size() > 0){
            sprite.setColor(availableColors.get(currentColor));
        } else {
            sprite.setColor(Color.WHITE);
        }
        if (alive || respawnTimer % .4f > .2f){
            sprite.draw(batch);
        }

        if (shot != null) shot.render(batch);
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
        if (alive && shot == null ){  //TODO: make this not shot if you have no colors
            shootLaser(screen.getPosFromScreen(screenX, screenY), Color.GREEN);
        }
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
        screen.camera.zoom += amount;

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
        return false;
    }

    public void kill(){
        if (respawnTimer <= 0){
            respawnTimer = 2f;
            shot = null;
            screen.game.tweenManager.killTarget(playerLight);
            playerLight.setColor(0,0,0,1);
            playerLight.setDistance(0);
        }
    }

    private void shootLaser(Vector2 target, Color color) {
        shot = new LaserShot(body, target, color);
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(playerLight, PointLightAccessor.DIST, 2.0f).target(10))
                .push(Tween.to(playerLight, PointLightAccessor.RGB, 2.0f).target(color.r, color.g, color.b).setCallback(new TweenCallback() {
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
    public void shotByPlayer(Color color) {

    }

    @Override
    public void shotByEnemy(Color color) {
        kill();
    }

    @Override
    public boolean collideWithBullet(Bullet bullet) {
        if (alive) {
            kill();
            return true;
        }
        return false;
    }
}
