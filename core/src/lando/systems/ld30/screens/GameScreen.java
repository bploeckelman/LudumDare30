package lando.systems.ld30.screens;

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
import lando.systems.ld30.*;
import lando.systems.ld30.enemies.*;
import lando.systems.ld30.tweens.PointLightAccessor;
import lando.systems.ld30.utils.*;

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
    public boolean allowSpawn = true;
    public int killsToBoss;
    public boolean bossSpawned;
    public boolean gotPowerUp = false;

    public PowerUp powerUp;
    public FinalBoss finalBoss;

    public boolean firstPop = false;
    public boolean secondPop = false;


    public GameScreen(LudumDare30 game) {
        this.game = game;
        firstPop = false;
        Globals.gameScreen = this;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.window_width / 10, Config.window_height / 10);
        camera.update();

        box2DDebugRenderer = new Box2DDebugRenderer();

        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(false);
        //.useDiffuseLight(RayHandler.BlendFunc.MULTIPLY);
        //RayHandler.useDiffuseLight(RayHandler.BlendFunc.OVERLAY);
        rayHandler = new RayHandler(Globals.world);
        rayHandler.setBlur(true);
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);
        rayHandler.setCulling(true);
        rayHandler.setAmbientLight(0.9f);
        setWorldColor();

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

        //enemies.add(new FinalBoss(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 0), this));


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
        light.setColor(0.2f, 0.2f, 0.2f, 1);
        light.setSoft(true);
        light.setDistance(20);
        Tween.to(light, PointLightAccessor.DIST, 5)
                .target(200)
                .repeatYoyo(-1,0)
                .start(game.tweenManager);


        light1 = new PointLight(rayHandler, num_rays);
        light1.setPosition(100, 100);
        light1.setColor(0, 1, 0, 1);
        light1.setDistance(100);
        light1.setActive(false);

        portals[0] = new Portal(new Color(1,0,0,1), Globals.red_center,    LEVEL_STATE.RED);
        portals[1] = new Portal(new Color(1,1,0,1), Globals.yellow_center, LEVEL_STATE.YELLOW);
        portals[2] = new Portal(new Color(0,1,0,1), Globals.green_center,  LEVEL_STATE.GREEN);
        portals[3] = new Portal(new Color(0,1,1,1), Globals.cyan_center,   LEVEL_STATE.CYAN);
        portals[4] = new Portal(new Color(0,0,1,1), Globals.blue_center,   LEVEL_STATE.BLUE);
        portals[5] = new Portal(new Color(1,0,1,1), Globals.purple_center, LEVEL_STATE.PURPLE);
    }

    float accum = 0;
    public void update(float dt){
        dt = Math.min (dt, 1/30f);

        if (ui.popup.isVisible()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.justTouched()) {
                ui.hidePopup();
            }
            return;
        }

        if (!firstPop){
            ui.showPopup("Our perfect white world was living\nin harmony for many eons.\n Until one day the world was \nshattered into 6 pieces by an evil prism \n\nClick to Dismiss" , "Welcome to Prismatic Worlds");
            firstPop = true;
        } else if (!secondPop){
            ui.showPopup("You should venture to the Red World\nit is over to the right. \n\nClick to Dismiss" , "First Objective");

            secondPop = true;
        }

        Stats.playTime += dt;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        // DEBUG
//        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) colorsBeat[0] = !colorsBeat[0];
//        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) colorsBeat[1] = !colorsBeat[1];
//        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) colorsBeat[2] = !colorsBeat[2];
//        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) colorsBeat[3] = !colorsBeat[3];
//        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) colorsBeat[4] = !colorsBeat[4];
//        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) colorsBeat[5] = !colorsBeat[5];

//        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
//            ui.showPopup("Read all the things!  (click or esc to dismiss) \n"
//                    + "................................................\n"
//                    + "................................................\n"
//                    + "................................................");
//        }

        // TODO: this is DEBUG
//        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
//            enterLevel(LEVEL_STATE.OVER_MAP, false);
//        }

        if (gotPowerUp){
            Globals.world.destroyBody(powerUp.body);
            powerUp = null;
            enterLevel(LEVEL_STATE.OVER_MAP, false);

        }

        if (allowSpawn && (enemies.size() == 0 || (Assets.random.nextFloat() > .997f && enemies.size() < MAX_ENEMIES))){
            int spawnColor = Assets.random.nextInt(6);
            if (colorsBeat[spawnColor]){
                spawnRandom(spawnColor);
            }
            switch (currentGameState){
                case RED:
                    spawnRandom(0);
                    break;
                case YELLOW:
                    spawnRandom(1);
                    break;
                case GREEN:
                    spawnRandom(2);
                    break;
                case CYAN:
                    spawnRandom(3);
                    break;
                case BLUE:
                    spawnRandom(4);
                    break;
                case PURPLE:
                    spawnRandom(5);
                    break;
            }
        }

        borderIntensity += dt;

        player.update(dt);
        int enemySize = enemies.size();
        for (int i = enemySize; --i >= 0;){
            Enemy enemy = enemies.get(i);
            enemy.update(dt);
            if (!enemy.alive) {

                if (enemy.isBoss){
                    allowSpawn = false;

                    powerUp = enemy.getPowerUP();
                    clearBoard();
                    break;
                }
                Globals.world.destroyBody(enemy.body);
                killsToBoss --;
                enemies.remove(i);
            }
        }

        checkSpawnBoss();

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



        if (onFinalBoss()) {
            if (!finalBoss.alive){
                // TODO change to credits screen
            }
            finalBoss.update(dt);
            if (!finalBoss.alive){
                ui.showPopup("Congrat", "You Win!");
            }
        }

        camera.position.lerp(new Vector3(player.body.getPosition().x, player.body.getPosition().y, 0f), .03f);

        camera.update();

        level.update(dt);

        ui.update(dt);
    }


    public void checkSpawnBoss(){
        if (killsToBoss <= 0 && !bossSpawned){
            switch (currentGameState){
                case RED:
                    bossSpawned = true;
                    enemies.add(new RedBoss(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 5), this));
                    break;
                case YELLOW:
                    bossSpawned = true;
                    enemies.add(new YellowBoss(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 5), this));
                    break;
                case GREEN:
                    bossSpawned = true;
                    enemies.add(new GreenBoss(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 5), this));
                    break;
                case CYAN:
                    bossSpawned = true;
                    enemies.add(new CyanBoss(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 5), this));
                    break;
                case BLUE:
                    bossSpawned = true;
                    enemies.add(new BlueBoss(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 5), this));
                    break;
                case PURPLE:
                    bossSpawned = true;
                    enemies.add(new PurpleBoss(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 5), this));
                    break;

            }
        }
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
        ambient.a = .2f;
        rayHandler.setAmbientLight(ambient);
    }

    public void enterLevel(LEVEL_STATE newState, boolean dead){
        LEVEL_STATE prevState = currentGameState;
        currentGameState = newState;
        setWorldColor();
        allowSpawn = true;
        gotPowerUp = false;
        bossSpawned = false;
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
        ui.showPopup("You don't have a weapon yet.\n\n  See if you can get them to hurt themselves");
        int enemiesOnSpawn = 8;
        for (int i = 0; i < enemiesOnSpawn; i ++)    {
            enemies.add(new RedEnemy(new Vector2(30,0).rotate(360/enemiesOnSpawn * i).add(Globals.red_center)  , this));
        }

        killsToBoss = 10;


    }

    public void enterGreenLevel(){
        ui.showPopup("These are some feisty guys.\n\nWatch out!!");
        int enemiesOnSpawn = 12;
        for (int i = 0; i < enemiesOnSpawn; i ++)    {
            enemies.add(new GreenEnemy(new Vector2(30,0).rotate(360/enemiesOnSpawn * i).add(Globals.green_center)  , this));
        }

        killsToBoss = 15;
    }

    public void enterYellowLevel(){
        ui.showPopup("You can get a gun from these guys.");
        int enemiesOnSpawn = 12;
        for (int i = 0; i < enemiesOnSpawn; i ++)    {
            enemies.add(new YellowEnemy(new Vector2(30,0).rotate(360/enemiesOnSpawn * i).add(Globals.yellow_center)  , this));
        }
        killsToBoss = 15;
    }

    public void enterCyanLevel(){
        ui.showPopup("Ohhh heat seeking missles?");
        int enemiesOnSpawn = 12;
        for (int i = 0; i < enemiesOnSpawn; i ++)    {
            enemies.add(new CyanEnemy(new Vector2(30,0).rotate(360/enemiesOnSpawn * i).add(Globals.cyan_center)  , this));
        }
        killsToBoss = 15;
    }

    public void enterBlueLevel(){
        ui.showPopup("Shield Technology?");
        int enemiesOnSpawn = 16;
        for (int i = 0; i < enemiesOnSpawn; i ++)    {
            enemies.add(new BlueEnemy(new Vector2(30,0).rotate(360/enemiesOnSpawn * i).add(Globals.blue_center)  , this));
        }
        killsToBoss = 20;
    }

    public void enterPurpleLevel(){
        int enemiesOnSpawn = 16;
        for (int i = 0; i < enemiesOnSpawn; i ++)    {
            enemies.add(new PurpleEnemy(new Vector2(30,0).rotate(360/enemiesOnSpawn * i).add(Globals.purple_center)  , this));
        }
        killsToBoss = 30;
    }

    public void returnToOverMap(LEVEL_STATE prevState, boolean dead){
        if (!dead) {
            player.hitPoints = player.max_hit_points;
            switch (prevState) {
                case RED:
                    ui.showPopup("Now that you have a weapon.\nLets go into the other worlds!");
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

        if (onFinalBoss()){
            allowSpawn = false;
            if (finalBoss == null)
                finalBoss = new FinalBoss(new Vector2( Globals.world_center_x +  0, Globals.world_center_y + 0), this);

        }

    }

    public void clearBoard(){
        for (Enemy enemy : enemies){
            Globals.world.destroyBody(enemy.body);
        }
        enemies.clear();
        for (Bullet bullet : bullets){
            Globals.world.destroyBody(bullet.body);
        }
        bullets.clear();
    }

    public boolean onFinalBoss(){
        boolean complete = true;
        for (boolean level : colorsBeat){
            if (!level) complete = false;
        }
        if (complete && !finalPop){
           finalPop = true;
           ui.showPopup("Now is the time to reconnect\nour world.  Destroy the evil prism!");
        }
        return complete;
    }

    boolean finalPop = false;

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

        if (powerUp != null){
            powerUp.render(Assets.batch);
        }

        if (onFinalBoss()) finalBoss.render(Assets.batch);

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
        if (ui.popup.isVisible()) {
            return false;
        }

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
