package lando.systems.ld30;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.primitives.MutableFloat;
import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Globals;

/**
 * Brian Ploeckelman created on 8/24/2014.
 */
public class SpinnyLights {

    public static final int num_lights = 6;
    public static final float light_dist = 400;
    public static final float cone_degrees = 20;

    public ConeLight[] lights;
    public MutableFloat[] directions;
    public Color color;
    public Vector2 position;

    public SpinnyLights(Color color, Vector2 position) {
        this.color = color;
        this.color.a = .6f;
        this.position = position;

        lights = new ConeLight[num_lights];
        directions = new MutableFloat[num_lights];

        float degrees = 0;
        for (int i = 0; i < num_lights; ++i) {
            directions[i] = new MutableFloat(degrees += 360 / num_lights);
            lights[i] = new ConeLight(Globals.gameScreen.rayHandler, Globals.gameScreen.num_rays,
                    color, light_dist, position.x, position.y,
                    directions[i].floatValue(), cone_degrees);
        }

        initializeTweens();
    }

    public void disable() {
        for (int i = 0; i < num_lights; ++i) {
            lights[i].setActive(false);
            Globals.game.tweenManager.killTarget(directions[i]);
        }
    }

    public void enable() {
        float degrees = 0;
        for (int i = 0; i < num_lights; ++i) {
            directions[i].setValue(degrees += 360 / num_lights);
            lights[i].setDirection(directions[i].floatValue());
            lights[i].setActive(true);
        }
        initializeTweens();
    }

    public void update() {
        for (int i = 0; i < num_lights; ++i) {
            lights[i].setDirection(directions[i].floatValue());
        }
    }

    private void initializeTweens() {
        for (int i = 0; i < num_lights; ++i) {
            Tween.to(directions[i], 0, 4.5f)
                    .target(directions[i].floatValue() + 360)
                    .ease(Linear.INOUT)
                    .repeat(-1, 0)
                    .start(Globals.game.tweenManager);
        }
    }

}
