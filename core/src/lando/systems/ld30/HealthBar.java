package lando.systems.ld30;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld30.utils.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class HealthBar {

    public MutableFloat value;
    public Rectangle bounds;


    public HealthBar(float width, float height) {
        value = new MutableFloat(0);
        bounds = new Rectangle(0, 0, width, height);
    }

    public void setWidth(float width){
        bounds.width = width;
    }

    public void setPosition(float x, float y) {
        bounds.x = x;
        bounds.y = y;
    }

    public void setPosition(Vector2 pos) {
        bounds.x = pos.x;
        bounds.y = pos.y;
    }

    public void setValue(float value) {
        if (value < 0 || value > 1) return;
        this.value.setValue(value);
    }

    public void render(SpriteBatch batch) {
        final float pad_left   = Assets.ninepatchGreen.getPadLeft();
        final float pad_right  = Assets.ninepatchGreen.getPadRight();
        final float pad_top    = Assets.ninepatchGreen.getPadTop();
        final float pad_bottom = Assets.ninepatchGreen.getPadBottom();

        Assets.ninepatchGrey.draw(batch, bounds.x + pad_left, bounds.y + pad_bottom,
                (bounds.width - pad_left - pad_right), bounds.height - pad_top - pad_bottom);
        if (value.floatValue() <= 0) return;
        Assets.ninepatchGreen.draw(batch, bounds.x + pad_left, bounds.y + pad_bottom,
                (bounds.width - pad_left - pad_right) * value.floatValue(), bounds.height - pad_top - pad_bottom);
    }

}
