package lando.systems.ld30.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;

/**
 * Brian Ploeckelman created on 8/23/2014.
 */
public class PointLightAccessor implements TweenAccessor<PointLight> {

    public static final int DIST = 1;
    public static final int R = 2;
    public static final int G = 3;
    public static final int B = 4;
    public static final int RGB = 5;

    @Override
    public int getValues(PointLight target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case DIST:  returnValues[0] = target.getDistance(); return 1;
            case R:  returnValues[0] = target.getColor().r; return 1;
            case G:  returnValues[0] = target.getColor().g; return 1;
            case B:  returnValues[0] = target.getColor().b; return 1;
            case RGB:
                returnValues[0] = target.getColor().r;
                returnValues[1] = target.getColor().g;
                returnValues[2] = target.getColor().b;
                return 3;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(PointLight target, int tweenType, float[] newValues) {
        final Color c = target.getColor();
        switch (tweenType) {
            case DIST:  target.setDistance(newValues[0]); break;
            case R:     target.setColor(newValues[0], c.g, c.b, c.a); break;
            case G:     target.setColor(c.r, newValues[0], c.b, c.a); break;
            case B:     target.setColor(c.r, c.g, newValues[0], c.a); break;
            case RGB:
                target.setColor(newValues[0], newValues[1], newValues[2], c.a);
                break;
            default: assert false;
        }
    }

}
