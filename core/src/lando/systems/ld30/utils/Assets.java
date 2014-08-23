package lando.systems.ld30.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

    public static TextureAtlas atlas;

    public static Random random;
    public static ShaderProgram prismProgram;

    public static void load() {
        random = new Random();
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();

        badlogic = new Texture("badlogic.jpg");
        badlogic.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        final String VERTEX = Gdx.files.internal("prism.vert").readString();
        final String FRAG = Gdx.files.internal("prism.frag").readString();
        prismProgram = new ShaderProgram(VERTEX, FRAG);
        //Good idea to log any warnings if they exist
        if (prismProgram.getLog().length()!=0)
            System.out.println(prismProgram.getLog());

        atlas = new TextureAtlas(Gdx.files.internal("atlas/game.atlas"));
    }

    public static void dispose() {
        atlas.dispose();
        badlogic.dispose();
        shapes.dispose();
        batch.dispose();
    }

}
