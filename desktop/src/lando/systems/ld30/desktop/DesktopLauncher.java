package lando.systems.ld30.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.utils.Config;

public class DesktopLauncher {
    public static void main (String[] arg) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.filterMin = Texture.TextureFilter.MipMapNearestNearest;
        settings.filterMag = Texture.TextureFilter.MipMapNearestNearest;
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;
        TexturePacker.process(settings, "./images", "./atlas", "game");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Config.window_width;
        config.height = Config.window_height;
        config.title = Config.title;
        new LwjglApplication(new LudumDare30(), config);
    }
}
