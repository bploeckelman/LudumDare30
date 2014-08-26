package lando.systems.ld30.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.utils.Config;

public class DesktopLauncher {


    final static boolean PARSE_SVG = false;

    public static void main (String[] arg) {
//
//        if (PARSE_SVG) {
//
//            SVGConverter svgc = new SVGConverter();
//            try {
//                svgc.run("../../art/svg-to-parse");
//            } catch (Exception e) {
//                System.out.println("failed to parse svgs");
//            }
//
//        }
//
//        TexturePacker.Settings settings = new TexturePacker.Settings();
//        settings.filterMin = Texture.TextureFilter.MipMapNearestNearest;
//        settings.filterMag = Texture.TextureFilter.MipMapNearestNearest;
////        settings.filterMin = Texture.TextureFilter.Linear;
////        settings.filterMag = Texture.TextureFilter.Linear;
//        settings.maxWidth = 1024;
//        settings.maxHeight = 1024;
//        TexturePacker.process(settings, "./images", "./atlas", "game");
////        TexturePacker.process(settings, "./prism", "./atlas", "prism");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Config.window_width;
        config.height = Config.window_height;
        config.title = Config.title;
        new LwjglApplication(new LudumDare30(), config);
    }
}
