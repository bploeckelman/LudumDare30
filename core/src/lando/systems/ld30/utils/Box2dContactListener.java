package lando.systems.ld30.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import lando.systems.ld30.Bullet;
import lando.systems.ld30.Player;
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
    public final static short CATEGORY_PLAYER_BULLET = 0x010;

    public final static short MASK_PLAYER = CATEGORY_BULLET | CATEGORY_ENEMY | CATEGORY_WORLD;
    public final static short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_WORLD | CATEGORY_ENEMY |CATEGORY_PLAYER_BULLET;
    public final static short MASK_BULLET = CATEGORY_PLAYER | CATEGORY_WORLD;
    public final static short MASK_WORLD = CATEGORY_BULLET | CATEGORY_ENEMY | CATEGORY_PLAYER | CATEGORY_PLAYER_BULLET;
    public final static short MASK_PLAYER_BULLET = CATEGORY_ENEMY | CATEGORY_WORLD;
    public final static short MASK_DEAD = CATEGORY_WORLD;

    public Box2dContactListener(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {
        if (!contact.isTouching()) {
            return;
        }

        Collidable collidableA = (Collidable) contact.getFixtureA().getBody().getUserData();
        Collidable collidableB = (Collidable) contact.getFixtureB().getBody().getUserData();

        final CollidableType typeA = (collidableA != null) ? collidableA.getType() : CollidableType.MISC;
        final CollidableType typeB = (collidableB != null) ? collidableB.getType() : CollidableType.MISC;

        Gdx.app.log("CONTACT", "typeA(" + collidableA + ") = " + typeA.toString() + ", typeB(" + collidableB + ") = " + typeB.toString());

        if (collidableB.getType() == CollidableType.BULLET){
            Collidable temp = collidableB;
            collidableB = collidableA;
            collidableA = temp;
        }  else if (collidableB.getType() == CollidableType.WORLD) {
            Collidable temp = collidableB;
            collidableB = collidableA;
            collidableA = temp;
        }  else  if (collidableB.getType() == CollidableType.PLAYER){
            Collidable temp = collidableB;
            collidableB = collidableA;
            collidableA = temp;
        }

        // TODO : figure out which fixture is what body and respond appropriately
        switch (collidableA.getType()){
            case BULLET:
                if (collidableB.collideWithBullet((Bullet)collidableA))
                    ((Bullet) collidableA).die();
                break;
            case WORLD:
                collidableB.collideWithWorld();
                break;
            case PLAYER:
                collidableB.collisionDamage(((Player)collidableA).body.getLinearVelocity().len() + collidableB.getVelocity());
                collidableA.collisionDamage(collidableB.getVelocity());
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
