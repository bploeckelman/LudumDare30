package lando.systems.ld30.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

/**
 * Assets - Contains all game assets
 */
public class Assets {

    public static SpriteBatch batch;
    public static ShapeRenderer shapes;

    public static BitmapFont font;
    public static BitmapFont hudFont;

    public static Texture badlogic;
    public static Texture beam;
    public static Texture rainbow;
    public static Texture background;
    public static Texture circle;

    public static NinePatch ninepatchBrown;
    public static NinePatch ninepatchGreen;
    public static NinePatch ninepatchGrey;
    public static NinePatch ninepatchSmall;

    public static TextureAtlas atlas;
    public static TextureAtlas prism;
    public static Music gameMusic;
    public static Music titleMusic;

    public static Random random;
    public static ShaderProgram prismProgram;
    public static ShaderProgram shimmerProgram;


    public static ParticleEffect explodeParticleEffect;
    public static ParticleEffect playerDeathParticleEffect;

    public static void load() {
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();

        font = new BitmapFont(Gdx.files.internal("font/crystalline.fnt"), false);
        hudFont = new BitmapFont();

        badlogic = new Texture("badlogic.jpg");
        badlogic.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        beam = new Texture("beam.png");
        beam.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        rainbow = new Texture("rainbow.png");
        rainbow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        circle = new Texture("circle.png");
        circle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = new Texture("background.png");
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        random = new Random();

        explodeParticleEffect = new ParticleEffect();
        explodeParticleEffect.load(Gdx.files.internal("explode.p"), Gdx.files.internal("images"));

        playerDeathParticleEffect = new ParticleEffect();
        playerDeathParticleEffect.load(Gdx.files.internal("player-death.p"), Gdx.files.internal("images"));
        Assets.playerDeathParticleEffect.scaleEffect(0.07f);

        final String VERTEX = Gdx.files.internal("prism.vert").readString();
        final String FRAG = Gdx.files.internal("prism.frag").readString();
        prismProgram = new ShaderProgram(VERTEX, FRAG);
        //Good idea to log any warnings if they exist
        if (prismProgram.getLog().length() != 0)
            System.out.println(prismProgram.getLog());

        final String RAINFRAG = Gdx.files.internal("rainbow.frag").readString();
        shimmerProgram = new ShaderProgram(VERTEX, RAINFRAG);
        //Good idea to log any warnings if they exist
        if (shimmerProgram.getLog().length() != 0)
            System.out.println(shimmerProgram.getLog());

        atlas = new TextureAtlas(Gdx.files.internal("atlas/game.atlas"));
        prism = new TextureAtlas(Gdx.files.internal("atlas/prism.atlas"));

        ninepatchBrown = new NinePatch(atlas.findRegion("brown-panel"), 10, 10, 10, 10);
        ninepatchGreen = new NinePatch(atlas.findRegion("green-bar"), 7, 7, 7, 7);
        ninepatchGrey = new NinePatch(atlas.findRegion("white-bar"), 7, 7, 7, 7);
        ninepatchSmall = new NinePatch(atlas.findRegion("ninepatch-small"), 3, 3, 3, 3);

        ninepatchBrown.setColor(new Color(153, 102, 51, 1));

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("ld30backgroundmusic.mp3"));
        gameMusic.setLooping(true);

        titleMusic = Gdx.audio.newMusic(Gdx.files.internal("ld30titlemusic.mp3"));
        titleMusic.setLooping(true);
        titleMusic.play();


    }

    public static void dispose() {
        hudFont.dispose();
        font.dispose();
        playerDeathParticleEffect.dispose();
        explodeParticleEffect.dispose();
        atlas.dispose();
        prism.dispose();
        badlogic.dispose();
        shapes.dispose();
        batch.dispose();
        gameMusic.dispose();
    }

}
