package lando.systems.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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

    public Player (Vector2 position){
        this.speed = 100;
        this.position = position;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(20f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        this.body = Globals.world.createBody(bodyDef);
        this.body.createFixture(circleShape, 1);
        circleShape.dispose();
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            position.y += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            position.y -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            position.x -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            position.x += speed* dt;
        }

        body.setTransform(position, 0);
    }

    public void render(SpriteBatch batch){
        batch.draw(Assets.badlogic, position.x - 10, position.y - 10, 20, 20);
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
