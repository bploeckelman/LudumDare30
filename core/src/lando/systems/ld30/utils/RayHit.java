package lando.systems.ld30.utils;

import java.util.Comparator;

/**
 * Created by dsgraham on 8/24/14.
 */
public class RayHit implements Comparable<RayHit> {
    public Collidable collidable;
    public float fraction;

    public RayHit (Collidable h, float f){
        collidable = h;
        fraction = f;
    }

    @Override
    public int compareTo(RayHit o) {
        return Float.compare(fraction, o.fraction);
    }
}
