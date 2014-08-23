package lando.systems.ld30.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.utils.Config;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Config.window_width, Config.window_height);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new LudumDare30();
        }
}