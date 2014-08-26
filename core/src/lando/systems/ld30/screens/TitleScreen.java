package lando.systems.ld30.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Config;
import lando.systems.ld30.utils.Globals;


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

    float accum = 0;
    TextureRegion keyframe;
    boolean tweenRunning = false;
    boolean launchGame = false;

    private Animation prismShimmerAnim;
    private float animTimer = 0;

    public TitleScreen(LudumDare30 game){
        super();
         this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.window_width, Config.window_height);
        fbo = new FrameBuffer(Pixmap.Format.RGB888, Config.window_width, Config.window_height, false);
        rainbowSprite = new Sprite(Assets.rainbow);
        rainbowSprite.setSize(600, 300);
        rainbowSprite.setCenter(Config.window_width/2, Config.window_height/2);

        initializeAnimations();
    }

    public void update(float dt){
        if (tweenRunning) return;

        final float duration = prismShimmerAnim.getAnimationDuration();

        animTimer += dt;
        if (animTimer >= duration) {
            animTimer = 0;
        }
        shimmerAccum += dt;

        if (Gdx.input.justTouched()){
            accum = 0;
            launchGame = true;
        }

        if (launchGame
         && (animTimer >= duration / 2 - 0.05f && animTimer <= duration / 2 + 0.05f)) {
            tweenRunning = true;
            keyframe = Assets.prism.findRegion("prism-colored-v1");
            Assets.titleSound.play(0.8f);
            Tween.to(prismScale, 0, 2.0f)
                    .target(3)
                    .delay(0)
                    .ease(Linear.INOUT)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(callbackEnd)
                    .start(game.tweenManager);
            return;
        }

        keyframe = prismShimmerAnim.getKeyFrame(animTimer);
    }


    @Override
    public void render(float delta) {

        update(delta);
        fbo.begin();
        camera.update();
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth() / 2;
        float h = Gdx.graphics.getHeight() / 2;
//        float ww = keyframe.getRegionWidth() / 2;
//        float hh = keyframe.getRegionHeight() / 2;

        Assets.batch.setProjectionMatrix(camera.combined);
        Assets.batch.begin();  // THINGS THAT NEED TO get prismed go here

        Assets.batch.draw(keyframe, w - 250, h - 250, 500, 500);

        Assets.font.setScale(1.5f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(Assets.batch, "Prismatic Worlds", 100, Gdx.graphics.getHeight() - 100 );

        Assets.batch.end();
        fbo.end();

//        Gdx.gl20.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl20.glClearColor(1,1,1,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.batch.begin();
        Assets.batch.setShader(Assets.shimmerProgram);
        Assets.shimmerProgram.setUniformf("u_time", shimmerAccum);
        Assets.shimmerProgram.setUniformf("u_alpha", .1f);
        rainbowSprite.draw(Assets.batch);

        Assets.batch.setShader(Assets.prismProgram);
        Assets.prismProgram.setUniformf("u_scale", prismScale.floatValue());
        accum+=3*delta;
        Assets.prismProgram.setUniformf("u_dir", (float)Math.sin(accum), (float)Math.cos(accum));

        Assets.batch.draw(fbo.getColorBufferTexture(), 0, fbo.getHeight(), fbo.getWidth(), -fbo.getHeight());
        Assets.batch.setShader(null);

        Assets.hudFont.setScale(1.5f);
        Assets.hudFont.setColor(Color.BLUE);
        BitmapFont.TextBounds bounds = Assets.hudFont.getBounds("Click to Begin!");
        Assets.hudFont.draw(Assets.batch, "Click to Begin!", (Gdx.graphics.getWidth() - bounds.width)/2f, (bounds.height + 50));
        // THINGS THAT ARE STATIC GO IN HERE

        Assets.batch.end();

    }

    private TweenCallback callbackEnd = new TweenCallback(){
        @Override
        public void onEvent(int type, BaseTween<?> source){
            Assets.titleMusic.stop();
            Assets.gameMusic.play();
            Assets.prism.dispose();
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

    private void initializeAnimations() {
        prismShimmerAnim = new Animation(0.05f,
                Assets.prism.findRegion("prism-shimmer-v101"),
                Assets.prism.findRegion("prism-shimmer-v102"),
                Assets.prism.findRegion("prism-shimmer-v103"),
                Assets.prism.findRegion("prism-shimmer-v104"),
                Assets.prism.findRegion("prism-shimmer-v105"),
                Assets.prism.findRegion("prism-shimmer-v106"),
                Assets.prism.findRegion("prism-shimmer-v107"),
                Assets.prism.findRegion("prism-shimmer-v108"),
                Assets.prism.findRegion("prism-shimmer-v109"),

                Assets.prism.findRegion("prism-shimmer-v110"),
                Assets.prism.findRegion("prism-shimmer-v111"),
                Assets.prism.findRegion("prism-shimmer-v112"),
                Assets.prism.findRegion("prism-shimmer-v113"),
                Assets.prism.findRegion("prism-shimmer-v114"),
                Assets.prism.findRegion("prism-shimmer-v115"),
                Assets.prism.findRegion("prism-shimmer-v116"),
                Assets.prism.findRegion("prism-shimmer-v117"),
                Assets.prism.findRegion("prism-shimmer-v118"),
                Assets.prism.findRegion("prism-shimmer-v119"),

                Assets.prism.findRegion("prism-shimmer-v120"),
                Assets.prism.findRegion("prism-shimmer-v121"),
                Assets.prism.findRegion("prism-shimmer-v122"),
                Assets.prism.findRegion("prism-shimmer-v123"),
                Assets.prism.findRegion("prism-shimmer-v124"),
                Assets.prism.findRegion("prism-shimmer-v125"),
                Assets.prism.findRegion("prism-shimmer-v126"),
                Assets.prism.findRegion("prism-shimmer-v127"),
                Assets.prism.findRegion("prism-shimmer-v128"),
                Assets.prism.findRegion("prism-shimmer-v129"),

                Assets.prism.findRegion("prism-shimmer-v130"),
                Assets.prism.findRegion("prism-shimmer-v131"),
                Assets.prism.findRegion("prism-shimmer-v132"),
                Assets.prism.findRegion("prism-shimmer-v133"),
                Assets.prism.findRegion("prism-shimmer-v134"),
                Assets.prism.findRegion("prism-shimmer-v135"),
                Assets.prism.findRegion("prism-shimmer-v136"),
                Assets.prism.findRegion("prism-shimmer-v137"),
                Assets.prism.findRegion("prism-shimmer-v138"),
                Assets.prism.findRegion("prism-shimmer-v139"),

                Assets.prism.findRegion("prism-shimmer-v140"),
                Assets.prism.findRegion("prism-shimmer-v141"),
                Assets.prism.findRegion("prism-shimmer-v142"),
                Assets.prism.findRegion("prism-shimmer-v143"),
                Assets.prism.findRegion("prism-shimmer-v144"),
                Assets.prism.findRegion("prism-shimmer-v145"),
                Assets.prism.findRegion("prism-shimmer-v146"),
                Assets.prism.findRegion("prism-shimmer-v147"),
                Assets.prism.findRegion("prism-shimmer-v148"),
                Assets.prism.findRegion("prism-shimmer-v149"),

                Assets.prism.findRegion("prism-shimmer-v150"),
                Assets.prism.findRegion("prism-shimmer-v151"),
                Assets.prism.findRegion("prism-shimmer-v152"),
                Assets.prism.findRegion("prism-shimmer-v153"),
                Assets.prism.findRegion("prism-shimmer-v154"),
                Assets.prism.findRegion("prism-shimmer-v155"),
                Assets.prism.findRegion("prism-shimmer-v156"),
                Assets.prism.findRegion("prism-shimmer-v157"),
                Assets.prism.findRegion("prism-shimmer-v158"),
                Assets.prism.findRegion("prism-shimmer-v159"),

                Assets.prism.findRegion("prism-shimmer-v160")
        );
        prismShimmerAnim.setPlayMode(Animation.PlayMode.LOOP);

        animTimer = 0;
    }

}
