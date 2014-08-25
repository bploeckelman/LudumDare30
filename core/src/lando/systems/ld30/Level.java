package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.*;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Brian Ploeckelman created on 8/24/2014.
 */
public class Level implements Collidable {

    GameScreen screen;
    public Body body;

    ArrayList<ParticleEffect> particleEffects;
    ParticleEffectPool particleEffectPool;
    public FixtureDef fixtureDef;

    public Level(GameScreen screen) {
        this.screen = screen;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);

        body = Globals.world.createBody(bodyDef);
        body.setUserData(this);


        ChainShape chainShape = new ChainShape();
        Vector2[] tempVerts = createChamber(0);
        Vector2[] fullVerts = new Vector2[tempVerts.length*6];
        for (int i = 0; i < 6; i ++){
            tempVerts = createChamber(360 - (i*60));
            for (int j = 0; j < tempVerts.length; j++){
                fullVerts[j+(i*tempVerts.length)] = tempVerts[j];
            }
        }
        chainShape.createLoop(fullVerts);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;
        fixtureDef.density = 1;
        fixtureDef.filter.categoryBits = Box2dContactListener.CATEGORY_WORLD;
        fixtureDef.filter.maskBits = Box2dContactListener.MASK_WORLD;

        body.createFixture(fixtureDef);

        chainShape.dispose();

        particleEffectPool = new ParticleEffectPool(Assets.explodeParticleEffect, 0, 20);
        particleEffects = new ArrayList<ParticleEffect>();
    }



    public Vector2[] createChamber(float angle){
        Vector2 offset = new Vector2(0,0).sub(183,0).rotate(angle+180).add(Globals.world_center_x, Globals.world_center_y);
        Vector2[] items = new Vector2[] {
                new Vector2 (0,200),
                new Vector2 (0,125),
                new Vector2 (75,125),
                new Vector2 (75, 200),
                new Vector2 (175,200),
                new Vector2 (175,0),
                new Vector2 (75, 0),
                new Vector2 (75,75),
                new Vector2 (0, 75),
                new Vector2 (0,0)
        };
        for (Vector2 vert : items){
            vert.sub(0, 100).rotate(angle).add(offset);


        }
        return items;
    }

    public void update(float dt) {
        ParticleEffect effect;
        for (int i = particleEffects.size() - 1; i >= 0; --i) {
            effect = particleEffects.get(i);
            if (effect.isComplete()) {
                particleEffects.remove(i);
            } else {
                effect.update(dt);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (ParticleEffect effect : particleEffects) {
            effect.draw(batch);
        }
    }

    @Override
    public CollidableType getType() {
        return CollidableType.WORLD;
    }

    @Override
    public void shotByPlayer(Color color) {

    }

    @Override
    public void shotByEnemy(LaserShot laser) {

    }

    @Override
    public boolean collideWithBullet(Bullet bullet) {
        ParticleEffect particleEffect = particleEffectPool.obtain();
        particleEffect.setPosition(bullet.body.getPosition().x, bullet.body.getPosition().y);
        particleEffect.scaleEffect(0.075f);
        particleEffects.add(particleEffect);
        return true;
    }

    @Override
    public void collideWithWorld() {

    }

    @Override
    public void collisionDamage(float damage) {

    }

    @Override
    public float getVelocity() {
        return 0;
    }
}
