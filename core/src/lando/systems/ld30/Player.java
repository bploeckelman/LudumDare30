package lando.systems.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Globals;

/**
 * Created by dsgraham on 8/23/14.
 */
public class Player implements InputProcessor{

    private static final BodyDef bodyDef = new BodyDef();

    public Body body;
    public Vector2 position;
    public float speed;
    public Sprite sprite;

    public Player (Vector2 position){
        this.speed = .5f;
        this.position = position;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(.1f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        this.body = Globals.world.createBody(bodyDef);
        this.body.createFixture(circleShape, 1f);
//        body.setLinearDamping(.1f);
        circleShape.dispose();

        sprite = new Sprite(Assets.badlogic);
        sprite.setSize(.2f,.2f);
        sprite.setOriginCenter();
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            body.applyForceToCenter(0, speed, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            body.applyForceToCenter(0, -speed, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            body.applyForceToCenter(-speed, 0, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            body.applyForceToCenter(speed, 0, true);
        }

        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        //body.setTransform(position, 0);
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
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
}
