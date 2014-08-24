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

        final String userDataA = (String) contact.getFixtureA().getBody().getUserData();
        final String userDataB = (String) contact.getFixtureB().getBody().getUserData();
        if (userDataA == null || userDataB == null) {
            return;
        }

        // TODO : figure out which fixture is what body and respond appropriately

        Gdx.app.log("CONTACT", "userDataA = " + userDataA + ", userDataB = " + userDataB);
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
