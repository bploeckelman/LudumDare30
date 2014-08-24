package lando.systems.ld30.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.*;
import lando.systems.ld30.Enemy;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.Player;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Config;
import lando.systems.ld30.utils.Globals;

import java.util.ArrayList;

/**
 * Created by dsgraham on 8/23/14.
 */
public class GameScreen implements Screen {

    private final LudumDare30 game;
    private final OrthographicCamera camera;

    Box2DDebugRenderer box2DDebugRenderer;

    RayHandler rayHandler;

    PointLight light, light1;

    ArrayList<Body> balls = new ArrayList<Body>();

    Player player;
    Enemy enemy;

    final int num_rays = 256;


    public GameScreen(LudumDare30 game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.window_width / 10, Config.window_height / 10);
        camera.update();

        box2DDebugRenderer = new Box2DDebugRenderer();

        rayHandler = new RayHandler(Globals.world);
        setWorldColor(new Color(.5f,.5f,.5f,.5f));
        rayHandler.setShadows(true);
        rayHandler.setCulling(true);


        light = new PointLight(rayHandler, num_rays);
        light.setColor(1, 0, 0, 1);
        light.setDistance(200);
//        light.setActive(false);

        light1 = new PointLight(rayHandler, num_rays);
        light1.setPosition(100, 100);
        light1.setColor(0, 1, 0, 1);
        light1.setDistance(100);

        player = new Player(new Vector2(), this);
        camera.position.set(new Vector3(player.position, 0));

        enemy = new Enemy(new Vector2(0, 3), this);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(player);
        Gdx.input.setInputProcessor(multiplexer);

        BodyDef bodyDef = new BodyDef();
        float a = 0f;
        final float scale = 30;
        final float radius = 5f;
        for (int i = 0; i < 10; ++i, a += (2f * Math.PI) / 10f) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                    scale * (float) Math.cos(a),
                    scale * (float) Math.sin(a));

            Body ball = Globals.world.createBody(bodyDef);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(radius);
            ball.createFixture(circleShape, 0);
            circleShape.dispose();

            balls.add(ball);
        }
    }

    float accum = 0;
    public void update(float dt){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        player.update(dt);
        enemy.update(dt);
        camera.position.lerp(new Vector3(player.sprite.getX(), player.sprite.getY(), 0f), .03f);
        camera.update();
    }

    public void setWorldColor(Color color){
        rayHandler.setAmbientLight(color);
    }

    @Override
    public void render(float delta) {
        update(delta);
        boolean didStep = fixedStep(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.batch.setProjectionMatrix(camera.combined);
        Assets.batch.begin();

//        Assets.batch.draw(Assets.badlogic, 0, 0);

        for (Body body : balls) {
            Assets.batch.draw(Assets.badlogic, body.getPosition().x - 5f, body.getPosition().y - 5f, 10, 10);
        }

        enemy.render(Assets.batch);
        player.render(Assets.batch);

        Assets.batch.end();

        box2DDebugRenderer.render(Globals.world, camera.combined);

        rayHandler.setCombinedMatrix(camera.combined);
        if (didStep) rayHandler.update();
        rayHandler.render();
    }

    private final static int MAX_FPS = 30;
    private final static int MIN_FPS = 15;
    private final static float TIME_STEP = 1f / MAX_FPS;
    private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
    private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
    private final static int VELOCITY_ITERS = 6;
    private final static int POSITION_ITERS = 2;
    private float physicsTimeLeft;
    private boolean fixedStep(float delta) {
        physicsTimeLeft += delta;
        if (physicsTimeLeft > MAX_TIME_PER_FRAME)
            physicsTimeLeft = MAX_TIME_PER_FRAME;

        boolean stepped = false;
        while (physicsTimeLeft >= TIME_STEP) {
            Globals.world.step(TIME_STEP, VELOCITY_ITERS, POSITION_ITERS);
            physicsTimeLeft -= TIME_STEP;
            stepped = true;

            accum += TIME_STEP / 2f;
            light1.setPosition(
                    100 * (float) Math.sin(accum),
                    100 * (float) Math.cos(accum));
            light.setPosition(0,0);
        }
        return stepped;
    }


    public Vector2 getPosFromScreen(float x, float y) {
        Vector3 temp = camera.unproject(new Vector3(x, y, 0));
        return new Vector2(temp.x, temp.y);
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
        Globals.world.dispose();
    }
}
