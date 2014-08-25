package lando.systems.ld30;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld30.utils.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class HealthBar {

    public MutableFloat value;
    public Rectangle bounds;
    public Color baseColor;
    public Color barColor;
    public float verticalOffset;


    public HealthBar(float width, float height) {
        this(width, height, Color.DARK_GRAY.cpy(), Color.GREEN.cpy());
    }

    public HealthBar(float width, float height, Color baseColor, Color barColor) {
        this.value = new MutableFloat(1);
        this.bounds = new Rectangle(0, 0, width, height);
        this.baseColor = baseColor;
        this.barColor = barColor;
        this.verticalOffset = 0;
    }

    public void setColors(Color baseColor, Color barColor) {
        setBaseColor(baseColor);
        setBarColor(barColor);
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor = baseColor;
    }

    public void setBarColor(Color barColor) {
        this.barColor = barColor;
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
        final float pad_left   = Assets.ninepatchSmall.getPadLeft();
        final float pad_right  = Assets.ninepatchSmall.getPadRight();
        final float pad_top    = Assets.ninepatchSmall.getPadTop();
        final float pad_bottom = Assets.ninepatchSmall.getPadBottom();

        batch.setColor(baseColor);
        Assets.ninepatchGrey.draw(batch, bounds.x, bounds.y + verticalOffset, bounds.width, bounds.height);

        if (value.floatValue() > 0) {
            batch.setColor(barColor);
            Assets.ninepatchSmall.draw(batch, bounds.x + pad_left, bounds.y + pad_bottom + verticalOffset,
                    (bounds.width - pad_left - pad_right) * value.floatValue(), bounds.height - pad_top - pad_bottom);
        }
        batch.setColor(Color.WHITE);
    }

}
