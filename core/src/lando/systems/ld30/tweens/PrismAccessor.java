package lando.systems.ld30.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import lando.systems.ld30.Prism;

/**
 * Created by dsgraham on 8/23/14.
 */
public class PrismAccessor implements TweenAccessor<Prism> {

    public static final int SCALE = 1;

    @Override
    public int getValues(Prism target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case SCALE: returnValues[0] = target.scale; return 1;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(Prism target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case SCALE: target.scale = newValues[0]; break;
            default: assert false; break;
        }
    }
}
