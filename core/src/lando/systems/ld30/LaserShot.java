package lando.systems.ld30;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld30.utils.Assets;

/**
 * Created by dsgraham on 8/23/14.
 */
public class LaserShot {
    public float timeLeft;
    public boolean alive = false;
    public Sprite sprite;
    private Vector2 target;
    Player player;

    public LaserShot(Player player, Vector2 target){
        timeLeft = 2f;
        alive = true;
        sprite = new Sprite(Assets.beam);
        this.target = target;
        this.player = player;
    }

    public void update (float dt){
        timeLeft -= dt;
        if (timeLeft < 0){
            alive = false;
        }
    }

    public void render(SpriteBatch batch){
        float angle;
        float xDif = target.x - player.body.getPosition().x;
        float yDif = target.y - player.body.getPosition().y;
        float dir = yDif < 0 ? 180 : 0;
        if (xDif == 0){
           angle = dir;
        } else {
            angle = (float) (180 * Math.atan2(yDif, xDif) / Math.PI);
        }
        sprite.setSize(100 , .1f);
        sprite.setOrigin(0, sprite.getHeight()/2);

        sprite.setPosition(player.body.getPosition().x, player.body.getPosition().y);
        sprite.setRotation(angle);

        sprite.draw(batch);

    }
}
