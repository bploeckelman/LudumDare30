package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld30.utils.Globals;

/**
 * Created by dsgraham on 8/24/14.
 */
public class SeekingBullet extends Bullet{
    boolean fromPlayer;
    public SeekingBullet(Vector2 position, Vector2 dir, Color color, boolean fromPlayer, float speed, float damage) {
        super(position, dir, color, fromPlayer, speed, damage);
        this.fromPlayer = fromPlayer;
        body.setLinearDamping(.3f);
    }

    public void update(float dt)
    {
        super.update(dt);
        Vector2 dir = new Vector2();
        if (!fromPlayer){
            dir = (Globals.gameScreen.player.body.getPosition().cpy().sub(body.getPosition())).nor();
        }
        Vector2 vel = body.getLinearVelocity();
        // TODO: maybe make a max speed?
        body.applyForceToCenter(dir.scl(100), true);
    }
}
