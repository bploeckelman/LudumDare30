package lando.systems.ld30.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Config;


/**
 * Created by dsgraham on 8/23/14.
 */
public class TitleScreen implements Screen {

    private final OrthographicCamera camera;
    private final LudumDare30 game;
    private final FrameBuffer fbo;
    private MutableFloat prismScale = new MutableFloat(0);
    Sprite rainbowSprite;
    private float shimmerAccum =0;

    public TitleScreen(LudumDare30 game){
        super();
         this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.window_width, Config.window_height);
        fbo = new FrameBuffer(Pixmap.Format.RGB888, Config.window_width, Config.window_height, false);
        rainbowSprite = new Sprite(Assets.rainbow);
        rainbowSprite.setCenter(Config.window_width/2, Config.window_height/2);
    }

    public void update(float dt){
        if (Gdx.input.justTouched()){
            accum = 0;
            Tween.to(prismScale, 0, .5f)
                    .target(.5f)
                    .delay(0)
                    .ease(Linear.INOUT)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(callbackEnd)
                    .start(game.tweenManager);
        }
        shimmerAccum += dt/1;
    }


    float accum = 0;

    @Override
    public void render(float delta) {

        update(delta);
        fbo.begin();
        camera.update();
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.batch.setProjectionMatrix(camera.combined);
        Assets.batch.begin();  // THINGS THAT NEED TO get prismed go here
        Assets.batch.draw(Assets.badlogic, 500, 100);
        Assets.batch.end();
        fbo.end();

        Gdx.gl20.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.batch.begin();
        Assets.batch.setShader(Assets.shimmerProgram);
        Assets.shimmerProgram.setUniformf("u_time", shimmerAccum);
        Assets.shimmerProgram.setUniformf("u_alpha", .3f);
        rainbowSprite.draw(Assets.batch);

        Assets.batch.setShader(Assets.prismProgram);
        Assets.prismProgram.setUniformf("u_scale", prismScale.floatValue());
        accum+=delta;
        Assets.prismProgram.setUniformf("u_dir", (float)Math.sin(accum), (float)Math.cos(accum));

        Assets.batch.draw(fbo.getColorBufferTexture(), 0, fbo.getHeight(), fbo.getWidth(), -fbo.getHeight());
        Assets.batch.setShader(null);

        // THINGS THAT ARE STATIC GO IN HERE

        Assets.batch.end();

    }

    private TweenCallback callbackEnd = new TweenCallback(){
        @Override
        public void onEvent(int type, BaseTween<?> source){
            game.setScreen(new GameScreen(game));
        }

    };

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
