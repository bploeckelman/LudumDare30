package lando.systems.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lando.systems.ld30.enemies.Enemy;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Assets;

/**
 * Brian Ploeckelman created on 8/24/2014.
 */
public class UserInterface {

    public OrthographicCamera hudCamera;

    public Skin skin;
    public Stage stage;

    public Dialog popup;

    public final GameScreen screen;

    final float margin_x = 5;
    final float margin_y = 5;
    final float popup_width = 400;
    final float popup_height = 250;

    public UserInterface(GameScreen screen) {
        this.screen = screen;

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudCamera.update();

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        initializeWidgets();
    }

    public void update(float delta) {
        stage.act(delta);
        hudCamera.update();

        popup.setCenterPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        updateHealthBars();
    }

    public void render() {
        stage.draw();
        if (!popup.isVisible()) {
            renderHealthBars();
        }
    }

    public void resize(int width, int height) {
        hudCamera.setToOrtho(false, width, height);
        hudCamera.update();

        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }

    public void showPopup(String text) {
        showPopup(text, null);
    }

    public void showPopup(String text, String title) {
        popup.getContentTable().clear();
        popup.setTitle((title != null) ? title : "Note");
        popup.text(text);
        popup.setVisible(true);
    }

    public void hidePopup() {
        popup.setVisible(false);
    }

    private void initializeWidgets() {
        popup = new Dialog("Popup text", skin);
        popup.setSize(popup_width, popup_height);
        popup.setKeepWithinStage(true);
        popup.setBackground(new TextureRegionDrawable(Assets.atlas.findRegion("brown-panel")));
        popup.setCenterPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        popup.setVisible(false);

        stage.addActor(popup);
    }

    Vector3 p = new Vector3();
    private void updateHealthBars() {
        final Viewport v = stage.getViewport();

        // Update the player's health bar
        p.set(screen.player.body.getPosition().x, screen.player.body.getPosition().y, 0);
        screen.camera.project(p, v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());
        screen.player.healthBar.setPosition(
                p.x - screen.player.healthBar.bounds.width / 2,
                p.y + screen.player.healthBar.bounds.height / 2 + 5);
        screen.player.shieldBar.setPosition(
                p.x - screen.player.shieldBar.bounds.width / 2,
                p.y + screen.player.shieldBar.bounds.height + screen.player.healthBar.bounds.height + 5);

        // Update enemy health bars
        for (Enemy enemy : screen.enemies) {
            if (!enemy.alive) continue;

            p.set(enemy.body.getPosition().x, enemy.body.getPosition().y, 0);
            screen.camera.project(p, v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());
            enemy.healthBar.setPosition(
                    p.x - enemy.healthBar.bounds.width / 2,
                    p.y + enemy.healthBar.bounds.height / 2);
            if (enemy.shieldBar != null) {
                enemy.shieldBar.setPosition(
                        p.x - enemy.shieldBar.bounds.width / 2,
                        p.y + enemy.shieldBar.bounds.height + enemy.healthBar.bounds.height - 1);
            }
        }
    }

    private void renderHealthBars() {
        stage.getBatch().begin();
        if (screen.player.alive) {
            screen.player.healthBar.render((SpriteBatch) stage.getBatch());
            if (screen.player.isShieldUp()) {
                screen.player.shieldBar.render((SpriteBatch) stage.getBatch());
            }
        }
        for (Enemy enemy : screen.enemies) {
            if (!enemy.alive) continue;
            enemy.healthBar.render((SpriteBatch) stage.getBatch());
            if (enemy.shieldBar != null && enemy.isShieldUp()) {
                enemy.shieldBar.render((SpriteBatch) stage.getBatch());
            }
        }
        stage.getBatch().end();
    }

}
