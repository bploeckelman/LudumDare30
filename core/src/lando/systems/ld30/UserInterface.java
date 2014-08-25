package lando.systems.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lando.systems.ld30.screens.GameScreen;

/**
 * Brian Ploeckelman created on 8/24/2014.
 */
public class UserInterface {

    public OrthographicCamera hudCamera;

    public Skin skin;
    public Stage stage;

    public HealthBar playerHealthBar;
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

        stage.getBatch().begin();
        playerHealthBar.render((SpriteBatch) stage.getBatch());
        stage.getBatch().end();
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

        playerHealthBar = new HealthBar(100, 30);
        playerHealthBar.setValue(1);
    }

    Vector3 p = new Vector3();
    private void updateHealthBars() {
        final Viewport v = stage.getViewport();


        // Update the player's health bar
        p.set(screen.player.body.getPosition().x, screen.player.body.getPosition().y, 0);
        screen.camera.project(p, v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());
        playerHealthBar.setPosition(p.x - playerHealthBar.bounds.width / 2, p.y + screen.player.fixture.getShape().getRadius() + 1);
        playerHealthBar.setValue(screen.player.getPercentHP());
    }

}
