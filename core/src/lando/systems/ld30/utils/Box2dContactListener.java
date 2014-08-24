package lando.systems.ld30.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import lando.systems.ld30.Bullet;
import lando.systems.ld30.screens.GameScreen;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public class Box2dContactListener implements ContactListener {

    public GameScreen screen;

    public final static short CATEGORY_PLAYER = 0x001;
    public final static short CATEGORY_ENEMY = 0x002;
    public final static short CATEGORY_BULLET = 0x004;
    public final static short CATEGORY_WORLD = 0x008;

    public final static short MASK_PLAYER = CATEGORY_BULLET | CATEGORY_ENEMY | CATEGORY_WORLD;
    public final static short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_WORLD;
    public final static short MASK_BULLET = CATEGORY_PLAYER | CATEGORY_WORLD;
    public final static short MASK_WORLD = CATEGORY_BULLET | CATEGORY_ENEMY | CATEGORY_PLAYER;

    public Box2dContactListener(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {
        if (!contact.isTouching()) {
            return;
        }

        final Collidable collidableA = (Collidable) contact.getFixtureA().getBody().getUserData();
        final Collidable collidableB = (Collidable) contact.getFixtureB().getBody().getUserData();

        final CollidableType typeA = (collidableA != null) ? collidableA.getType() : CollidableType.MISC;
        final CollidableType typeB = (collidableB != null) ? collidableB.getType() : CollidableType.MISC;

        Gdx.app.log("CONTACT", "typeA(" + collidableA + ") = " + typeA.toString() + ", typeB(" + collidableB + ") = " + typeB.toString());


        // TODO : figure out which fixture is what body and respond appropriately
        switch (typeB){
            case BULLET:
                if (collidableA == null) {
                    ((Bullet) collidableB).die();
                    break;
                }
                collidableA.collideWithBullet((Bullet)collidableB);
                ((Bullet) collidableB).die();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
