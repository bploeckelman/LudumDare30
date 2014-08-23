package lando.systems.ld30.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.math.Vector3;

import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.Player;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Config;

/**
 * Created by dsgraham on 8/23/14.
 */
public class GameScreen implements Screen {

    private final LudumDare30 game;
    private final OrthographicCamera camera;


    RayHandler rayHandler;
    World world;

    PointLight light, light1;


    Player player;


    public GameScreen (LudumDare30 game){
        this.game = game;



        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.window_width, Config.window_height);

        camera.update();

        world = new World(new Vector2(0, 0), true);

        rayHandler = new RayHandler(world);

        light = new PointLight(rayHandler, 32);
        light.setColor(1,0,0,1);
        light.setDistance(100);

        light1 = new PointLight(rayHandler, 32);
        light1.setPosition(30, 30);
        light1.setColor(0,1,0,1);
        light1.setDistance(50);

        player = new Player(Vector2.Zero);
        camera.position.set(new Vector3(player.position, 0));

        InputMultiplexer multiplexer = new InputMultiplexer();

        multiplexer.addProcessor(player);
        Gdx.input.setInputProcessor(multiplexer);

    }

    public void update(float dt){
        player.update(dt);
        camera.position.lerp(new Vector3(player.position, 0f), .03f);
        camera.update();

        final float win_center_x = Config.window_width / 2;
        final float win_center_y = Config.window_height / 2;

        accum += 2*Gdx.graphics.getDeltaTime();
        light1.setPosition(
                50 * (float) Math.sin(accum) + win_center_x,
                50 * (float) Math.cos(accum) + win_center_y);
        light.setPosition(win_center_x, win_center_y);

        world.step(dt, 8, 3);
    }

    float accum = 0;
    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        Assets.batch.setProjectionMatrix(camera.combined);
        Assets.batch.begin();

        player.render(Assets.batch);

        Assets.batch.end();

        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.setAmbientLight(1f,1f,1f,.2f);
        rayHandler.updateAndRender();


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
        rayHandler.dispose();
        world.dispose();
    }
}
