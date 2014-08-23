package lando.systems.ld30;

import com.badlogic.gdx.Game;
import lando.systems.ld30.Screens.TitleScreen;
import lando.systems.ld30.utils.Assets;

public class LudumDare30 extends Game {

    @Override
    public void create () {
        Assets.load();

        TitleScreen titleScreen = new TitleScreen(this);
        setScreen(titleScreen);
    }


    @Override
    public void dispose() {
        Assets.dispose();
    }
}
