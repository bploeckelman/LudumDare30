package lando.systems.ld30.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import lando.systems.ld30.Bullet;
import lando.systems.ld30.EnemyLaserShot;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Collidable;
import lando.systems.ld30.utils.CollidableType;
import lando.systems.ld30.utils.Globals;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public abstract class Enemy implements Collidable {

    protected static final BodyDef bodyDef = new BodyDef();

    public EnemyLaserShot shot;

    public float body_radius;

    public GameScreen screen;

    public Body body;
    public Sprite sprite;
    public Animation animation;

    public float speed;
    public float animTimer;
    public float bulletSpeed = 50;

    public boolean alive;

    // Temporary helper vectors for calculating all the things
    protected Vector2 dist = new Vector2();
    protected Vector2 dir = new Vector2();

    public Enemy(Vector2 position, GameScreen screen) {
        this.screen = screen;
        alive = true;

        initializeBox2dBody(position);
        intializeSprite();
    }

    public void update(float dt) {
        animTimer += dt;
        sprite.setRegion(animation.getKeyFrame(animTimer));
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
    }

    public void render(SpriteBatch batch) {
        if (shot != null) shot.render(batch);
        sprite.draw(batch);
    }

    public void kill(){
        alive = false;
        Globals.world.destroyBody(body);
    }

    public void shootBullet(Vector2 target){
        Vector2 dir = target.cpy().sub(body.getPosition()).scl(bulletSpeed);
        screen.bullets.add(new Bullet(body.getPosition().cpy(), dir, Color.WHITE));
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
    public void shotByEnemy(Color color) {

    }

    @Override
    public boolean collideWithBullet(Bullet bullet) {
          return false;
    }

    protected abstract void intializeSprite();
    protected abstract void initializeBox2dBody(Vector2 position);

}
