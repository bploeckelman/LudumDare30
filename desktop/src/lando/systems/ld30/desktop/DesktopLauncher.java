package lando.systems.ld30.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.utils.Config;

public class DesktopLauncher {

    final static boolean PARSE_SVG = true;

    public static void main (String[] arg) {

        if (PARSE_SVG) {

            SVGConverter svgc = new SVGConverter();
            try {
                svgc.run("../../art/svg-to-parse");
            } catch (Exception e) {
                System.out.println("failed to parse svgs");
            }

        } else {

            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.width = Config.window_width;
            config.height = Config.window_height;
            config.title = Config.title;
            new LwjglApplication(new LudumDare30(), config);

        }


    }


}
