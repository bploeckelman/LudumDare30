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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import lando.systems.ld30.*;
import lando.systems.ld30.enemies.*;
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
    public enum LEVEL_STATE {PRE_RED , RED, OVER_MAP, YELLOW, GREEN, CYAN, BLUE, PURPLE};
    public boolean[] colorsBeat = new boolean[6];

    public LEVEL_STATE currentGameState = LEVEL_STATE.PRE_RED;
    Box2DDebugRenderer box2DDebugRenderer;

    public RayHandler rayHandler;

    Color borderColor = Color.GREEN;
    float borderIntensity = 0;

    PointLight light, light1;
    Portal[] portals = new Portal[6];

    public int MAX_ENEMIES = 50;

    public Level level;
    public Player player;
    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    public UserInterface ui;
    public final int num_rays = 512;


    public GameScreen(LudumDare30 game) {
        this.game = game;
        Globals.gameScreen = this;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.window_width / 10, Config.window_height / 10);
        camera.update();

        box2DDebugRenderer = new Box2DDebugRenderer();

        rayHandler = new RayHandler(Globals.world);
        setWorldColor();
        rayHandler.setShadows(true);
        rayHandler.setCulling(true);

        initializeChamberLights();

        player = new Player(new Vector2(Globals.world_center_x, Globals.world_center_y), this);
        camera.position.set(new Vector3(player.body.getPosition(), 0));
        camera.zoom = 1.5f;

//        enemies.add(new RedEnemy(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 5), this));
//        enemies.add(new RedEnemy(new Vector2( Globals.world_center_x +  5, Globals.world_center_y + 0), this));
//        enemies.add(new RedEnemy(new Vector2( Globals.world_center_x +  0,Globals.world_center_y +  -5), this));
//        enemies.add(new RedEnemy(new Vector2( Globals.world_center_x + -5, Globals.world_center_y + 0), this));
//        enemies.add(new YellowEnemy(new Vector2( Globals.world_center_x + -7,Globals.world_center_y + -7), this));
//        enemies.add(new YellowEnemy(new Vector2( Globals.world_center_x + -7,Globals.world_center_y +  7), this));
//        enemies.add(new YellowEnemy(new Vector2( Globals.world_center_x +  7,Globals.world_center_y +  7), this));
//        enemies.add(new YellowEnemy(new Vector2( Globals.world_center_x +  7,Globals.world_center_y + -7), this));
//        enemies.add(new GreenEnemy(new Vector2( Globals.world_center_x +  0, Globals.world_center_y +  9), this));
//        enemies.add(new GreenEnemy(new Vector2( Globals.world_center_x +  9, Globals.world_center_y +  0), this));
//        enemies.add(new GreenEnemy(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + -9), this));
//        enemies.add(new GreenEnemy(new Vector2( Globals.world_center_x + -9, Globals.world_center_y +  0), this));
//        enemies.add(new BlueEnemy(new Vector2( Globals.world_center_x + -11,Globals.world_center_y + -11), this));
//        enemies.add(new BlueEnemy(new Vector2( Globals.world_center_x + -11,Globals.world_center_y +  11), this));
//        enemies.add(new BlueEnemy(new Vector2( Globals.world_center_x +  11,Globals.world_center_y +  11), this));
//        enemies.add(new BlueEnemy(new Vector2( Globals.world_center_x +  11,Globals.world_center_y + -11), this));
//        enemies.add(new CyanEnemy(new Vector2( Globals.world_center_x + -11,Globals.world_center_y + -11), this));
//        enemies.add(new CyanEnemy(new Vector2( Globals.world_center_x + -11,Globals.world_center_y +  11), this));
//        enemies.add(new CyanEnemy(new Vector2( Globals.world_center_x +  11,Globals.world_center_y +  11), this));
//        enemies.add(new CyanEnemy(new Vector2( Globals.world_center_x +  11,Globals.world_center_y + -11), this));
//        enemies.add(new PurpleEnemy(new Vector2( Globals.world_center_x + -11,Globals.world_center_y + -11), this));
//        enemies.add(new PurpleEnemy(new Vector2( Globals.world_center_x + -11,Globals.world_center_y +  11), this));
//        enemies.add(new PurpleEnemy(new Vector2( Globals.world_center_x +  11,Globals.world_center_y +  11), this));
//        enemies.add(new PurpleEnemy(new Vector2( Globals.world_center_x +  11,Globals.world_center_y + -11), this));

        //enemies.add(new RedBoss(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 5), this));

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(player);
        Gdx.input.setInputProcessor(multiplexer);

        Globals.world.setContactListener(new Box2dContactListener(this));

        level = new Level(this);
        portals[0].activate();

        ui = new UserInterface(this);
    }

    private void initializeChamberLights() {
        light = new PointLight(rayHandler, num_rays);
        light.setColor(1, 0, 0, 1);
        light.setDistance(200);

        light1 = new PointLight(rayHandler, num_rays);
        light1.setPosition(100, 100);
        light1.setColor(0, 1, 0, 1);
        light1.setDistance(100);

        portals[0] = new Portal(new Color(1,0,0,1), Globals.red_center,    LEVEL_STATE.RED,    this);
        portals[1] = new Portal(new Color(1,1,0,1), Globals.yellow_center, LEVEL_STATE.YELLOW, this);
        portals[2] = new Portal(new Color(0,1,0,1), Globals.green_center,  LEVEL_STATE.GREEN,  this);
        portals[3] = new Portal(new Color(0,1,1,1), Globals.cyan_center,   LEVEL_STATE.CYAN,   this);
        portals[4] = new Portal(new Color(0,0,1,1), Globals.blue_center,   LEVEL_STATE.BLUE,   this);
        portals[5] = new Portal(new Color(1,0,1,1), Globals.purple_center, LEVEL_STATE.PURPLE, this);
    }

    float accum = 0;
    public void update(float dt){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        // TODO: this is DEBUG
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            enterLevel(LEVEL_STATE.OVER_MAP, false);
        }

        if (Assets.random.nextFloat() > .995f && enemies.size() < MAX_ENEMIES){
            int spawnColor = Assets.random.nextInt(6);
            if (colorsBeat[spawnColor]){
                spawnRandom(spawnColor);
            }
        }

        borderIntensity += dt;

        player.update(dt);
        int enemySize = enemies.size();
        for (int i = enemySize; --i >= 0;){
            Enemy enemy = enemies.get(i);
            enemy.update(dt);
            if (!enemy.alive) {
                Globals.world.destroyBody(enemy.body);
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

        for (int i = 0; i < portals.length; i ++){
            portals[i].update(dt);
            if (portals[i].playerInside(player.body.getPosition())){
                enterLevel(portals[i].nextState, false);
            }
        }

        camera.position.lerp(new Vector3(player.body.getPosition().x, player.body.getPosition().y, 0f), .03f);

        camera.update();

        level.update(dt);

        ui.update(dt);
    }


    public void spawnRandom(int color){
        Enemy enemy;
        Vector2 spawn = Globals.spawnPoints[Assets.random.nextInt(Globals.spawnPoints.length)].cpy().rotate(60 * Assets.random.nextInt(6)).add(Globals.worldCenter);
        switch (color){
            case 0:
                enemy = new RedEnemy(spawn, this);
                break;
            case 1:
                enemy = new YellowEnemy(spawn, this);
                break;
            case 2:
                enemy = new GreenEnemy(spawn, this);
                break;
            case 3:
                enemy = new CyanEnemy(spawn, this);
                break;
            case 4:
                enemy = new BlueEnemy(spawn, this);
                break;
            case 5:
                enemy = new PurpleEnemy(spawn, this);
                break;
            default:
                enemy = new RedEnemy(spawn, this);
        }

        enemies.add(enemy);
    }



    public void setWorldColor(){
        Color color = new Color(1,1,1,1);
        switch (currentGameState){
            case PRE_RED:
            case OVER_MAP:
                color = new Color(1,1,1,1);
                break;
            case RED:
                color = new Color(1, .5f, .5f,1);
                break;
            case YELLOW:
                color = new Color(1, 1f, .5f,1);
                break;
            case GREEN:
                color = new Color(.5f, 1f, .5f,1);
                break;
            case CYAN:
                color = new Color(.5f, 1f, 1f,1);
                break;
            case BLUE:
                color = new Color(.5f, .5f, 1f,1);
                break;
            case PURPLE:
                color = new Color(1f, .5f, 1f,1);
                break;
        }
        borderColor = color;
        Color ambient = color.cpy();
        ambient.a = .4f;
        rayHandler.setAmbientLight(ambient);
    }

    public void enterLevel(LEVEL_STATE newState, boolean dead){
        LEVEL_STATE prevState = currentGameState;
        currentGameState = newState;
        setWorldColor();
        for (int i = 0; i < portals.length; i++)
        {
            portals[i].deactivate();
        }
        switch(currentGameState){
            case RED:
                enterRedLevel();
                break;
            case YELLOW:
                enterYellowLevel();
                break;
            case GREEN:
                enterGreenLevel();
                break;
            case CYAN:
                enterCyanLevel();
                break;
            case BLUE:
                enterBlueLevel();
                break;
            case PURPLE:
                enterPurpleLevel();
                break;
            case OVER_MAP:
                returnToOverMap(prevState, dead);
                break;
        }
    }

    public void enterRedLevel(){
        int enemiesOnSpawn = 8;
        for (int i = 0; i < enemiesOnSpawn; i ++)    {
            enemies.add(new RedEnemy(new Vector2(30,0).rotate(360/enemiesOnSpawn * i).add(Globals.red_center)  , this));
        }

    }

    public void enterGreenLevel(){

    }

    public void enterYellowLevel(){
        int enemiesOnSpawn = 8;
        for (int i = 0; i < enemiesOnSpawn; i ++)    {
            enemies.add(new YellowEnemy(new Vector2(30,0).rotate(360/enemiesOnSpawn * i).add(Globals.yellow_center)  , this));
        }
    }

    public void enterCyanLevel(){

    }

    public void enterBlueLevel(){

    }

    public void enterPurpleLevel(){

    }

    public void returnToOverMap(LEVEL_STATE prevState, boolean dead){
        if (!dead) {
            switch (prevState) {
                case RED:
                    colorsBeat[0] = true;
                    player.availableColors.add(Globals.COLORS.RED);
                    break;
                case YELLOW:
                    colorsBeat[1] = true;
                    player.availableColors.add(Globals.COLORS.YELLOW);
                    break;
                case GREEN:
                    colorsBeat[2] = true;
                    player.availableColors.add(Globals.COLORS.GREEN);
                    break;
                case CYAN:
                    colorsBeat[3] = true;
                    player.availableColors.add(Globals.COLORS.CYAN);
                    break;
                case BLUE:
                    colorsBeat[4] = true;
                    player.availableColors.add(Globals.COLORS.BLUE);
                    break;
                case PURPLE:
                    colorsBeat[5] = true;
                    player.availableColors.add(Globals.COLORS.PURPLE);
                    break;

            }
        }
        if (!colorsBeat[0]){
            portals[0].activate();
        } else {
            for (int i = 0; i < colorsBeat.length; i++) {
                if (!colorsBeat[i]) portals[i].activate();
            }
        }
        for (Enemy enemy : enemies){
            Globals.world.destroyBody(enemy.body);
        }
        enemies.clear();
        for (Bullet bullet : bullets){
            Globals.world.destroyBody(bullet.body);
        }
        bullets.clear();

    }

    @Override
    public void render(float delta) {
        update(delta);
        boolean didStep = fixedStep(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        Assets.batch.setProjectionMatrix(camera.combined);
        Assets.batch.begin();

        Assets.batch.setColor(borderColor.cpy().mul(((float)(Math.sin(borderIntensity)+1.0)/8.0f) + .75f));
        Assets.batch.draw(Assets.background, 632, 629.9f, 736, 741);

        Assets.batch.setColor(Color.WHITE);

        for (int i = 0; i < portals.length; i ++){
            portals[i].render(Assets.batch);
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

        ui.render();
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
        ui.resize(width, height);
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
        ui.dispose();
        rayHandler.dispose();
        Globals.world.dispose();
    }
}
