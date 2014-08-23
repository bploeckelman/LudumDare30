package lando.systems.ld30.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.utils.Config;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Config.window_width;
        config.height = Config.window_height;
        new LwjglApplication(new LudumDare30(), config);
    }
}
