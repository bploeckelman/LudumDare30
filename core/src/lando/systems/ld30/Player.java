package lando.systems.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Collidable;
import lando.systems.ld30.utils.CollidableType;
import lando.systems.ld30.utils.Globals;

/**
 * Created by dsgraham on 8/23/14.
 */
public class Player implements InputProcessor, Collidable {

    private static final BodyDef bodyDef = new BodyDef();

    public Body body;
    public Vector2 position;
    public float speed;
    public Sprite sprite;
    private GameScreen screen;
    private LaserShot shot;

    public Player (Vector2 position, GameScreen screen){
        this.screen = screen;
        this.speed = 10f;
        this.position = position;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(2f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        body = Globals.world.createBody(bodyDef);
        body.createFixture(circleShape, .1f);
        body.setLinearDamping(1f);
        body.setAngularDamping(2f);
        body.setUserData(this);
        circleShape.dispose();

        sprite = new Sprite(Assets.badlogic);

        sprite.setOriginCenter();
        sprite.setSize(4f,4f);
    }

    private final float MAX_VELOCITY = 20f;
    public void update(float dt) {
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
        //body.setTransform(position, 0);
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
        if (shot != null) shot.render(batch);
        //batch.draw(Assets.badlogic, body.getPosition().x , body.getPosition().y - 10, 20, 20);
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
        if (shot == null){
           shot = new LaserShot(this, screen.getPosFromScreen(screenX, screenY), Color.GREEN);
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
        return false;
    }

    @Override
    public CollidableType getType() {
        return CollidableType.PLAYER;
    }
}
