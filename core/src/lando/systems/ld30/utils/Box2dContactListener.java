package lando.systems.ld30.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import lando.systems.ld30.screens.GameScreen;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public class Box2dContactListener implements ContactListener {

    public GameScreen screen;

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

        // TODO : figure out which fixture is what body and respond appropriately

        Gdx.app.log("CONTACT", "typeA(" + collidableA + ") = " + typeA.toString() + ", typeB(" + collidableB + ") = " + typeB.toString());
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
