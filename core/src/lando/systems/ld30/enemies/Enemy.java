package lando.systems.ld30.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Collidable;
import lando.systems.ld30.utils.CollidableType;
import lando.systems.ld30.utils.Globals;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public abstract class Enemy implements Collidable {

    protected static final BodyDef bodyDef = new BodyDef();

    public float body_radius;

    public GameScreen screen;

    public Body body;
    public Sprite sprite;
    public Animation animation;
    public TextureRegion keyframe;

    public float speed;
    public float animTimer;

    public boolean alive;

    public Enemy(Vector2 position, GameScreen screen) {
        this.screen = screen;
        alive = true;

        initializeBox2dBody(position);
        intializeSprite();
    }

    public void update(float dt) {
        animTimer += dt;
        keyframe = animation.getKeyFrame(animTimer);

        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));

        sprite.setTexture(keyframe.getTexture());
        sprite.setRegion(keyframe.getU(), keyframe.getV(), keyframe.getU2(), keyframe.getV2());
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void kill(){
        alive = false;
        Globals.world.destroyBody(body);
    }

    @Override
    public CollidableType getType() {
        return CollidableType.ENEMY;
    }

    @Override
    public void ShotByPlayer(Color color) {
         kill();
    }

    protected abstract void intializeSprite();
    protected abstract void initializeBox2dBody(Vector2 position);

}
