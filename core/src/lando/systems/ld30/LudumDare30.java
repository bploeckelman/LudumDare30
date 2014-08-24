package lando.systems.ld30;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import box2dLight.PointLight;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld30.screens.TitleScreen;
import lando.systems.ld30.tweens.ColorAccessor;
import lando.systems.ld30.tweens.PointLightAccessor;
import lando.systems.ld30.tweens.Vector2Accessor;
import lando.systems.ld30.tweens.Vector3Accessor;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Globals;

public class LudumDare30 extends Game {

    public TweenManager tweenManager;

    @Override
    public void create () {
        Assets.load();
        Globals.game = this;

        tweenManager = new TweenManager();
        Tween.registerAccessor(Color.class, new ColorAccessor());
        Tween.registerAccessor(Vector2.class, new Vector2Accessor());
        Tween.registerAccessor(Vector3.class, new Vector3Accessor());
        Tween.registerAccessor(PointLight.class, new PointLightAccessor());

        setScreen(new TitleScreen(this));
    }

    @Override
    public void render(){
        tweenManager.update(Gdx.graphics.getDeltaTime());

        super.render();
    }

    @Override
    public void dispose() {
        Assets.dispose();
    }
}
