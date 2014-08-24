package lando.systems.ld30;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import lando.systems.ld30.screens.TitleScreen;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Globals;

public class LudumDare30 extends Game {

    public TweenManager tManager;

    @Override
    public void create () {
        Assets.load();
        Globals.game = this;
        tManager = new TweenManager();
//        Tween.registerAccessor(Prism.class, new PrismAccessor());
        setScreen(new TitleScreen(this));
    }

    @Override
    public void render(){
        super.render();
        tManager.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        Assets.dispose();
    }
}
