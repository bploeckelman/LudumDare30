package lando.systems.ld30;

import com.badlogic.gdx.Game;

import lando.systems.ld30.screens.TitleScreen;
import lando.systems.ld30.utils.Assets;

public class LudumDare30 extends Game {

    @Override
    public void create () {
        Assets.load();

        setScreen(new TitleScreen(this));
    }

    @Override
    public void dispose() {
        Assets.dispose();
    }
}
