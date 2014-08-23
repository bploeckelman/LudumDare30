package lando.systems.ld30.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

	public static void load() {
		batch = new SpriteBatch();
		shapes = new ShapeRenderer();

		badlogic = new Texture("badlogic.jpg");

        random = new Random();

//		atlas = new TextureAtlas(Gdx.files.internal("atlas/game.atlas"));
	}

	public static void dispose() {
//		atlas.dispose();
		badlogic.dispose();
		shapes.dispose();
		batch.dispose();
	}

}
