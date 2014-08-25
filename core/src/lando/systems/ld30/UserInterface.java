package lando.systems.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lando.systems.ld30.enemies.Enemy;
import lando.systems.ld30.screens.GameScreen;

/**
 * Brian Ploeckelman created on 8/24/2014.
 */
public class UserInterface {

    public OrthographicCamera hudCamera;

    public Skin skin;
    public Stage stage;

    public Label playerHealthBarLabel;

    public final GameScreen screen;

    final float margin_x = 5;
    final float margin_y = 5;

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

        updateHealthBars();
    }

    public void render() {
        stage.draw();
        renderHealthBars();
    }

    public void resize(int width, int height) {
        hudCamera.setToOrtho(false, width, height);
        hudCamera.update();

        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }

    private void initializeWidgets() {
//        playerHealthBarLabel = new Label("Player: ", skin);
//        playerHealthBarLabel.setPosition(margin_x, margin_y);
//
//        stage.addActor(playerHealthBarLabel);
    }

    Vector3 p = new Vector3();
    private void updateHealthBars() {
        final Viewport v = stage.getViewport();

        // Update the player's health bar
        p.set(screen.player.body.getPosition().x, screen.player.body.getPosition().y, 0);
        screen.camera.project(p, v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());
        screen.player.healthBar.setPosition(
                p.x - screen.player.healthBar.bounds.width / 2,
                p.y + screen.player.healthBar.bounds.height / 2);

        // Update enemy health bars
        for (Enemy enemy : screen.enemies) {
            if (!enemy.alive) continue;

            p.set(enemy.body.getPosition().x, enemy.body.getPosition().y, 0);
            screen.camera.project(p, v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());
            enemy.healthBar.setPosition(
                    p.x - enemy.healthBar.bounds.width / 2,
                    p.y + enemy.healthBar.bounds.height / 2);
        }
    }

    private void renderHealthBars() {
        stage.getBatch().begin();
        if (screen.player.alive) {
            screen.player.healthBar.render((SpriteBatch) stage.getBatch());
        }
        for (Enemy enemy : screen.enemies) {
            if (!enemy.alive) continue;
            enemy.healthBar.render((SpriteBatch) stage.getBatch());
        }
        stage.getBatch().end();
    }

}
