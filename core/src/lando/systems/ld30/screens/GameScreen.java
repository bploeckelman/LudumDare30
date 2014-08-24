package lando.systems.ld30.screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import lando.systems.ld30.Bullet;
import lando.systems.ld30.Level;
import lando.systems.ld30.enemies.*;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.Player;
import lando.systems.ld30.tweens.PointLightAccessor;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Box2dContactListener;
import lando.systems.ld30.utils.Config;
import lando.systems.ld30.utils.Globals;

import java.util.ArrayList;

/**
 * Created by dsgraham on 8/23/14.
 */
public class GameScreen implements Screen {

    public final LudumDare30 game;
    public final OrthographicCamera camera;
    public Color[] colorsBeat = new Color[] {new Color(1,0,0,1), new Color(0,1,0,1), new Color(0,0,1,1),
                                             new Color(1,1,0,1), new Color(0,1,1,1), new Color(1,0,1,1)};

    Box2DDebugRenderer box2DDebugRenderer;

    public RayHandler rayHandler;

    Color borderColor = Color.GREEN;
    float borderIntensity = 0;

    PointLight light, light1;

    PointLight chamberLightRed, chamberLightCyan, chamberLightYellow, chamberLightGreen, chamberLightBlue, chamberLightPurple;

    ArrayList<Body> balls = new ArrayList<Body>();

    public Level level;
    public Player player;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    public final int num_rays = 512;


    public GameScreen(LudumDare30 game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.window_width / 10, Config.window_height / 10);
        camera.update();

        box2DDebugRenderer = new Box2DDebugRenderer();

        rayHandler = new RayHandler(Globals.world);
        setWorldColor(new Color(.5f, .5f, .5f, .5f));
        rayHandler.setShadows(true);
        rayHandler.setCulling(true);

        initializeChamberLights();

        player = new Player(new Vector2(Globals.world_center_x, Globals.world_center_y), this);
        camera.position.set(new Vector3(player.body.getPosition(), 0));

        enemies.add(new RedEnemy(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 5), this));
        enemies.add(new RedEnemy(new Vector2( Globals.world_center_x +  5, Globals.world_center_y + 0), this));
        enemies.add(new RedEnemy(new Vector2( Globals.world_center_x +  0,Globals.world_center_y +  -5), this));
        enemies.add(new RedEnemy(new Vector2( Globals.world_center_x + -5, Globals.world_center_y + 0), this));
        enemies.add(new YellowEnemy(new Vector2( Globals.world_center_x + -7,Globals.world_center_y + -7), this));
        enemies.add(new YellowEnemy(new Vector2( Globals.world_center_x + -7,Globals.world_center_y +  7), this));
        enemies.add(new YellowEnemy(new Vector2( Globals.world_center_x +  7,Globals.world_center_y +  7), this));
        enemies.add(new YellowEnemy(new Vector2( Globals.world_center_x +  7,Globals.world_center_y + -7), this));
        enemies.add(new GreenEnemy(new Vector2( Globals.world_center_x +  0, Globals.world_center_y +  9), this));
        enemies.add(new GreenEnemy(new Vector2( Globals.world_center_x +  9, Globals.world_center_y +  0), this));
        enemies.add(new GreenEnemy(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + -9), this));
        enemies.add(new GreenEnemy(new Vector2( Globals.world_center_x + -9, Globals.world_center_y +  0), this));
        enemies.add(new BlueEnemy(new Vector2( Globals.world_center_x + -11,Globals.world_center_y + -11), this));
        enemies.add(new BlueEnemy(new Vector2( Globals.world_center_x + -11,Globals.world_center_y +  11), this));
        enemies.add(new BlueEnemy(new Vector2( Globals.world_center_x +  11,Globals.world_center_y +  11), this));
        enemies.add(new BlueEnemy(new Vector2( Globals.world_center_x +  11,Globals.world_center_y + -11), this));

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
                    scale * (float) Math.cos(a) + Globals.world_center_x,
                    scale * (float) Math.sin(a) + Globals.world_center_y);

            Body ball = Globals.world.createBody(bodyDef);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(radius);
            FixtureDef fixture = new FixtureDef();
            fixture.shape = circleShape;
            fixture.density = 0f;
            fixture.filter.categoryBits = Box2dContactListener.CATEGORY_WORLD;
            fixture.filter.maskBits = Box2dContactListener.MASK_WORLD;
            ball.createFixture(fixture);
            circleShape.dispose();

            balls.add(ball);
        }

        Globals.world.setContactListener(new Box2dContactListener(this));

        level = new Level(this);
    }

    private void initializeChamberLights() {
        light = new PointLight(rayHandler, num_rays);
        light.setColor(1, 0, 0, 1);
        light.setDistance(200);

        light1 = new PointLight(rayHandler, num_rays);
        light1.setPosition(100, 100);
        light1.setColor(0, 1, 0, 1);
        light1.setDistance(100);

        chamberLightRed = new PointLight(rayHandler, num_rays);
        chamberLightRed.setPosition(Globals.red_center.x, Globals.red_center.y);
        chamberLightRed.setColor(Color.RED);
        chamberLightRed.setDistance(0);
        Timeline.createSequence()
                .push(Tween.to(chamberLightRed, PointLightAccessor.DIST, 2.5f).target(400))
                .push(Tween.to(chamberLightRed, PointLightAccessor.DIST, 2.5f).target(40))
                .repeatYoyo(-1, 0)
                .start(game.tweenManager);

        chamberLightCyan = new PointLight(rayHandler, num_rays);
        chamberLightCyan.setPosition(Globals.cyan_center.x, Globals.cyan_center.y);
        chamberLightCyan.setColor(Color.CYAN);
        chamberLightCyan.setDistance(0);
        Timeline.createSequence()
                .push(Tween.to(chamberLightCyan, PointLightAccessor.DIST, 2.5f).target(400))
                .push(Tween.to(chamberLightCyan, PointLightAccessor.DIST, 2.5f).target(40))
                .repeatYoyo(-1, 0)
                .start(game.tweenManager);

        chamberLightYellow = new PointLight(rayHandler, num_rays);
        chamberLightYellow.setPosition(Globals.yellow_center.x, Globals.yellow_center.y);
        chamberLightYellow.setColor(Color.YELLOW);
        chamberLightYellow.setDistance(0);
        Timeline.createSequence()
                .push(Tween.to(chamberLightYellow, PointLightAccessor.DIST, 2.5f).target(400))
                .push(Tween.to(chamberLightYellow, PointLightAccessor.DIST, 2.5f).target(40))
                .repeatYoyo(-1, 0)
                .start(game.tweenManager);

        chamberLightGreen = new PointLight(rayHandler, num_rays);
        chamberLightGreen.setPosition(Globals.green_center.x, Globals.green_center.y);
        chamberLightGreen.setColor(Color.GREEN);
        chamberLightGreen.setDistance(0);
        Timeline.createSequence()
                .push(Tween.to(chamberLightGreen, PointLightAccessor.DIST, 2.5f).target(400))
                .push(Tween.to(chamberLightGreen, PointLightAccessor.DIST, 2.5f).target(40))
                .repeatYoyo(-1, 0)
                .start(game.tweenManager);

        chamberLightBlue = new PointLight(rayHandler, num_rays);
        chamberLightBlue.setPosition(Globals.blue_center.x, Globals.blue_center.y);
        chamberLightBlue.setColor(Color.BLUE);
        chamberLightBlue.setDistance(0);
        Timeline.createSequence()
                .push(Tween.to(chamberLightBlue, PointLightAccessor.DIST, 2.5f).target(400))
                .push(Tween.to(chamberLightBlue, PointLightAccessor.DIST, 2.5f).target(40))
                .repeatYoyo(-1, 0)
                .start(game.tweenManager);

        chamberLightPurple = new PointLight(rayHandler, num_rays);
        chamberLightPurple.setPosition(Globals.purple_center.x, Globals.purple_center.y);
        chamberLightPurple.setColor(Color.PURPLE);
        chamberLightPurple.setDistance(0);
        Timeline.createSequence()
                .push(Tween.to(chamberLightPurple, PointLightAccessor.DIST, 2.5f).target(400))
                .push(Tween.to(chamberLightPurple, PointLightAccessor.DIST, 2.5f).target(40))
                .repeatYoyo(-1, 0)
                .start(game.tweenManager);
    }

    float accum = 0;
    public void update(float dt){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        borderIntensity += dt;

        player.update(dt);
        int enemySize = enemies.size();
        for (int i = enemySize; --i >= 0;){
            Enemy enemy = enemies.get(i);
            enemy.update(dt);
            if (!enemy.alive) {
                enemies.remove(i);
            }
        }

        int bulletSize = bullets.size();
        for (int i = bulletSize; --i >= 0;){
            Bullet bullet = bullets.get(i);
            bullet.update(dt);
            if (!bullet.alive) {
                Globals.world.destroyBody(bullet.body);
                bullets.remove(i);
            }
        }

        camera.position.lerp(new Vector3(player.sprite.getX(), player.sprite.getY(), 0f), .03f);
        camera.update();

        level.update(dt);
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

        Assets.batch.setColor(borderColor.cpy().mul(((float)(Math.sin(borderIntensity)+1.0)/4.0f) + .5f));
        Assets.batch.draw(Assets.background, 632, 629.9f, 736, 741);

        Assets.batch.setColor(Color.WHITE);

        for (Body body : balls) {
            Assets.batch.draw(player.animation.getKeyFrame(player.animTimer), body.getPosition().x - 5f, body.getPosition().y - 5f, 10, 10);
        }
        for (int i = 0; i < enemies.size(); i ++){
            enemies.get(i).render(Assets.batch);
        }

        for (int i = 0; i < bullets.size(); i ++){
            bullets.get(i).render(Assets.batch);
        }

        player.render(Assets.batch);

        level.render(Assets.batch);

        Assets.batch.end();

        //box2DDebugRenderer.render(Globals.world, camera.combined);

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

            accum += TIME_STEP;
            light1.setPosition(
                    40 * (float) Math.cos(accum) + Globals.world_center_x,
                    40 * (float) Math.sin(accum) + Globals.world_center_y);
            light.setPosition(Globals.world_center_x, Globals.world_center_y);
        }
        return stepped;
    }


    Vector3 projectTemp = new Vector3();
    public Vector2 getPosFromScreen(float x, float y) {
        camera.unproject(projectTemp.set(x, y, 0));
        return new Vector2(projectTemp.x, projectTemp.y);
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
