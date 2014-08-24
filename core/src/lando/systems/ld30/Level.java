package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Box2dContactListener;
import lando.systems.ld30.utils.Collidable;
import lando.systems.ld30.utils.CollidableType;
import lando.systems.ld30.utils.Globals;

/**
 * Brian Ploeckelman created on 8/24/2014.
 */
public class Level implements Collidable {

    GameScreen screen;
    Body body;

    public Level(GameScreen screen) {
        this.screen = screen;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);
        body = Globals.world.createBody(bodyDef);
        body.setUserData(this);

        Vector2 center = new Vector2();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(10, 100);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.filter.categoryBits = Box2dContactListener.CATEGORY_WORLD;
        fixtureDef.filter.maskBits = Box2dContactListener.MASK_WORLD;

        polygonShape.setAsBox(1, 100, center.set(-100, 0), 0);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef);

        polygonShape.setAsBox(1, 100, center.set( 100, 0), 0);
        fixtureDef.shape =  polygonShape;
        body.createFixture(fixtureDef);

        polygonShape.setAsBox(100, 1, center.set(0, -100), 0);
        fixtureDef.shape =  polygonShape;
        body.createFixture(fixtureDef);

        polygonShape.setAsBox(100, 1, center.set(0,  100), 0);
        fixtureDef.shape =  polygonShape;
        body.createFixture(fixtureDef);

        polygonShape.dispose();
    }


    @Override
    public CollidableType getType() {
        return CollidableType.WORLD;
    }

    @Override
    public void shotByPlayer(Color color) {

    }

    @Override
    public void shotByEnemy(Color color) {

    }

    @Override
    public boolean collideWithBullet(Bullet bullet) {
        // TODO : explosion or decal or something?
        return true;
    }
}
