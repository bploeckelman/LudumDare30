package lando.systems.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lando.systems.ld30.enemies.Enemy;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Stats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Brian Ploeckelman created on 8/24/2014.
 */
public class UserInterface {

    public OrthographicCamera hudCamera;

    public Skin skin;
    public Stage stage;

    public Image[] finishedColors;
    public Image colorSelection;

    public Label enemyKillsLabel;
    public Image[] enemyKillStatsIcons;
    public Label[] enemyKillStatsLabels;

    public Label playTimeLabel;

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
        updateColors();
        updateKillCounts();

        final int m = (int) (Stats.playTime / 60f);
        final float s = Stats.playTime - m * 60;
        playTimeLabel.setText("Time: " + String.format("%02d", m) + " min  " + String.format("%02.2f", s) + " sec");
        playTimeLabel.setPosition(
                stage.getWidth() / 2 - playTimeLabel.getTextBounds().width / 2,
                stage.getHeight() - playTimeLabel.getTextBounds().height - 3 * margin_y);
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
        popup.setTitle((title != null) ? "\n" + title : "\nNote");
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
        popup.setTitleAlignment(Align.top);
        stage.addActor(popup);

        playTimeLabel = new Label("Time: --", skin);
        playTimeLabel.setPosition(stage.getWidth() / 2 - playTimeLabel.getTextBounds().width / 2, stage.getHeight() - playTimeLabel.getTextBounds().height - margin_y);
        stage.addActor(playTimeLabel);

        final int num_colors = 6;
        finishedColors = new Image[num_colors];
        for (int i = 0; i < num_colors; ++i) {
            finishedColors[i] = new Image(Assets.atlas.findRegion("player0"));
            finishedColors[i].setColor(Color.LIGHT_GRAY.cpy());

            stage.addActor(finishedColors[i]);
        }

        colorSelection = new Image(Assets.atlas.findRegion("color-icon-select"));
        colorSelection.setVisible(false);
        stage.addActor(colorSelection);

        final float pad_left = 3 * margin_x;
        final float pad_bottom = 4 * margin_y;
        final float w = 24;

        enemyKillsLabel = new Label("Kills:", skin);
        enemyKillsLabel.setPosition(pad_left, pad_bottom + 20);
        enemyKillsLabel.setVisible(false);
        stage.addActor(enemyKillsLabel);

        float x = 0;
        enemyKillStatsLabels = new Label[num_colors];
        for (int i = 0; i < num_colors; ++i) {
            enemyKillStatsLabels[i] = new Label("0", skin);
            enemyKillStatsLabels[i].setPosition(pad_left + x + i * w, pad_bottom - margin_y);
            enemyKillStatsLabels[i].setVisible(false);

            stage.addActor(enemyKillStatsLabels[i]);

            x += w + margin_x;
        }

        x = 0;
        enemyKillStatsIcons = new Image[num_colors];
        enemyKillStatsIcons[0] = new Image(Assets.atlas.findRegion("red-enemy0"));
        enemyKillStatsIcons[1] = new Image(Assets.atlas.findRegion("yellow-enemy0"));
        enemyKillStatsIcons[2] = new Image(Assets.atlas.findRegion("green-enemy0"));
        enemyKillStatsIcons[3] = new Image(Assets.atlas.findRegion("cyan-enemy0"));
        enemyKillStatsIcons[4] = new Image(Assets.atlas.findRegion("blue-enemy00"));
        enemyKillStatsIcons[5] = new Image(Assets.atlas.findRegion("purple-enemy00"));
        for (int i = 0; i < num_colors; ++i) {
            enemyKillStatsIcons[i].setPosition(pad_left + x + i * w + enemyKillStatsLabels[i].getTextBounds().width + margin_x, pad_bottom);
            enemyKillStatsIcons[i].setVisible(false);

            stage.addActor(enemyKillStatsIcons[i]);

            x += w + margin_x;
        }

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

        if (screen.onFinalBoss()) {
            p.set(screen.finalBoss.body.getPosition().x, screen.finalBoss.body.getPosition().y, 0);
            screen.camera.project(p, v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());
            screen.finalBoss.healthBar.setPosition(
                    p.x - screen.finalBoss.healthBar.bounds.width / 2,
                    p.y + screen.finalBoss.healthBar.bounds.height / 2);
            if (screen.finalBoss.shieldBar != null) {
                screen.finalBoss.shieldBar.setPosition(
                        p.x - screen.finalBoss.shieldBar.bounds.width / 2,
                        p.y + screen.finalBoss.shieldBar.bounds.height + screen.finalBoss.healthBar.bounds.height - 1);
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
        if (screen.onFinalBoss()){
            screen.finalBoss.healthBar.render((SpriteBatch) stage.getBatch());
            if (screen.finalBoss.shieldBar != null && screen.finalBoss.isShieldUp()) {
                screen.finalBoss.shieldBar.render((SpriteBatch) stage.getBatch());
            }
        }
        stage.getBatch().end();
    }

    private static final Map<Integer, Color> color_map;
    static {
        HashMap<Integer, Color> map = new HashMap<Integer, Color>();
        map.put(0, Color.RED.cpy());
        map.put(1, Color.YELLOW.cpy());
        map.put(2, Color.GREEN.cpy());
        map.put(3, Color.CYAN.cpy());
        map.put(4, Color.BLUE.cpy());
        map.put(5, Color.PURPLE.cpy());
        color_map = Collections.unmodifiableMap(map);
    }
    private void updateColors() {
        final float pad_left = 3 * margin_x;
        final float pad_top = 4 * margin_y;
        final float w = 12;
        float x = 0;

        final int index = screen.player.getColorIndex();
        for (int i = 0; i < finishedColors.length; ++i) {
            finishedColors[i].setColor((screen.colorsBeat[i] ? color_map.get(i) : Color.LIGHT_GRAY.cpy()));
            finishedColors[i].setCenterPosition(pad_left + x + i * w, stage.getHeight() - pad_top);
            finishedColors[i].setZIndex(1);

            if (index == i) {
                colorSelection.setCenterPosition(pad_left + x + i * w, stage.getHeight() - pad_top);
                colorSelection.setVisible(true);
                colorSelection.setZIndex(0);
            }

            x += w + margin_x;
        }

        if (index == -1) {
            colorSelection.setVisible(false);
        }
    }

    private void updateKillCounts() {
        final float pad_left = 3 * margin_x;
        final float pad_bottom = 4 * margin_y;
        final float w = 24;
        float x = 0;

        for (int i = 0; i < enemyKillStatsLabels.length; ++i) {
            enemyKillStatsLabels[i].setPosition(pad_left + x + i * w, pad_bottom - margin_y);
            enemyKillStatsIcons[i].setPosition(pad_left + x + i * w + enemyKillStatsLabels[i].getTextBounds().width + margin_x, pad_bottom);

            enemyKillStatsLabels[i].setVisible(false);
            enemyKillStatsIcons[i].setVisible(false);

            x += w + margin_x;
        }

        // Set visibility and count
        if (Stats.redEnemiesKilled > 0) {
            enemyKillStatsLabels[0].setText("" + Stats.redEnemiesKilled);
            enemyKillStatsLabels[0].setVisible(true);
            enemyKillStatsIcons [0].setVisible(true);
        }
        if (Stats.yellowEnemiesKilled > 0) {
            enemyKillStatsLabels[1].setText("" + Stats.yellowEnemiesKilled);
            enemyKillStatsLabels[1].setVisible(true);
            enemyKillStatsIcons [1].setVisible(true);
        }
        if (Stats.greenEnemiesKilled > 0) {
            enemyKillStatsLabels[2].setText("" + Stats.greenEnemiesKilled);
            enemyKillStatsLabels[2].setVisible(true);
            enemyKillStatsIcons [2].setVisible(true);
        }
        if (Stats.cyanEnemiesKilled > 0) {
            enemyKillStatsLabels[3].setText("" + Stats.cyanEnemiesKilled);
            enemyKillStatsLabels[3].setVisible(true);
            enemyKillStatsIcons [3].setVisible(true);
        }
        if (Stats.blueEnemiesKilled > 0) {
            enemyKillStatsLabels[4].setText("" + Stats.blueEnemiesKilled);
            enemyKillStatsLabels[4].setVisible(true);
            enemyKillStatsIcons [4].setVisible(true);
        }
        if (Stats.purpleEnemiesKilled > 0) {
            enemyKillStatsLabels[5].setText("" + Stats.purpleEnemiesKilled);
            enemyKillStatsLabels[5].setVisible(true);
            enemyKillStatsIcons [5].setVisible(true);
        }

        if (Stats.haveKilled()) {
            enemyKillsLabel.setVisible(true);
        }
    }

}
