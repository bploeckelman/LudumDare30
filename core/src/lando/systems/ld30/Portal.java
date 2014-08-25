package lando.systems.ld30;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.tweens.PointLightAccessor;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Globals;

/**
 * Created by dsgraham on 8/24/14.
 */
public class Portal {
    public Color color;
    public SpinnyLights light;
    private float portalShimmer = 0;
    public Vector2 pos;
    public boolean active;
    public GameScreen.LEVEL_STATE nextState;
    public float portalRadius = 10;

    private final GameScreen screen;

    public Portal (Color color, Vector2 pos, GameScreen.LEVEL_STATE state, GameScreen screen) {
        this.screen = screen;
        nextState = state;
        this.pos = pos;
        active = false;

        light = new SpinnyLights(color, pos, screen.rayHandler, screen.num_rays, screen);
        light.disable();
    }

    public void update(float dt){
        portalShimmer += dt * 3;
        light.update();
    }

    public void activate(){
        active = true;
        light.enable();
    }

    public void deactivate(){
        active = false;
        light.disable();
    }

    public void render(SpriteBatch batch){
        if (!active) return;
        batch.setShader(Assets.shimmerProgram);
        Assets.shimmerProgram.setUniformf("u_time", portalShimmer);
        Assets.shimmerProgram.setUniformf("u_alpha", .3f);
        batch.draw(Assets.rainbow, pos.x - portalRadius, pos.y - portalRadius, 2* portalRadius,2 * portalRadius);
        batch.setShader(null);
    }

    public boolean playerInside(Vector2 playerPos){
         return active && pos.dst(playerPos)< portalRadius;
    }
}
