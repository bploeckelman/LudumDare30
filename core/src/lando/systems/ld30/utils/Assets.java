package lando.systems.ld30.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

/**
 * Assets - Contains all game assets
 */
public class Assets {

    public static SpriteBatch batch;
    public static ShapeRenderer shapes;

    public static Texture badlogic;
    public static Texture beam;
    public static Texture rainbow;

    public static TextureAtlas atlas;

    public static Random random;
    public static ShaderProgram prismProgram;
    public static ShaderProgram shimmerProgram;

    public static ParticleEffect explodeParticleEffect;

    public static void load() {
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();

        badlogic = new Texture("badlogic.jpg");
        badlogic.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        beam = new Texture("beam.png");
        beam.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        rainbow = new Texture("rainbow.png");
        rainbow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        random = new Random();

        explodeParticleEffect = new ParticleEffect();
        explodeParticleEffect.load(Gdx.files.internal("explode.p"), Gdx.files.internal("images"));

        final String VERTEX = Gdx.files.internal("prism.vert").readString();
        final String FRAG = Gdx.files.internal("prism.frag").readString();
        prismProgram = new ShaderProgram(VERTEX, FRAG);
        //Good idea to log any warnings if they exist
        if (prismProgram.getLog().length()!=0)
            System.out.println(prismProgram.getLog());

        final String RAINFRAG = Gdx.files.internal("rainbow.frag").readString();
        shimmerProgram = new ShaderProgram(VERTEX, RAINFRAG);
        //Good idea to log any warnings if they exist
        if (shimmerProgram.getLog().length()!=0)
            System.out.println(shimmerProgram.getLog());

        atlas = new TextureAtlas(Gdx.files.internal("atlas/game.atlas"));
    }

    public static void dispose() {
        explodeParticleEffect.dispose();
        atlas.dispose();
        badlogic.dispose();
        shapes.dispose();
        batch.dispose();
    }

}
