package lando.systems.ld30.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Config;

/**
 * Created by dsgraham on 8/23/14.
 */
public class GameScreen implements Screen {

    private final LudumDare30 game;
    private final OrthographicCamera camera;

    public GameScreen (LudumDare30 game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.window_width, Config.window_height);
    }

    public void update(float dt){

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl20.glClearColor(0.13f, 0.81f, 0.92f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.batch.begin();
        Assets.batch.draw(Assets.badlogic, 400, 100);
        Assets.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
