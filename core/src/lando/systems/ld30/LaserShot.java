package lando.systems.ld30;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by dsgraham on 8/23/14.
 */
public class LaserShot {
    public float timeLeft;
    public boolean alive = false;

    public LaserShot(Vector2 target){
        timeLeft = 2f;
        alive = true;
    }

    public void update (float dt){
        timeLeft -= dt;
        if (timeLeft < 0){
            alive = false;
        }
    }

    public void render(SpriteBatch batch){

    }
}
