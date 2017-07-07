package com.sebastian.sokolowski.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sebastian.sokolowski.game.MyGdxGame;

/**
 * Created by Sebastian Sokołowski on 05.04.17.
 */

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private static Integer score = 0;
    private static Integer energy = 0;
    private static Integer life = 3;

    static Label scoreLabel;
    static Label lifeLabel;

    private static Table dataTable;
    private static Image energyMask;
    private static Integer energyMax;

    public Hud(SpriteBatch spriteBatch) {
        score = 0;
        energy = 0;
        life = 3;

        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lifeLabel = new Label(life.toString(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Hud/panel.png")), 320, 100);
        Image panel = new Image(textureRegion);
        Table backgroundTable = new Table();
        backgroundTable.top();
        backgroundTable.setFillParent(true);
        backgroundTable.left();
        backgroundTable.add(panel).pad(10);

        stage.addActor(backgroundTable);

        dataTable = new Table();
        dataTable.top();
        dataTable.setFillParent(true);
        dataTable.left();
        dataTable.pad(10);
        dataTable.padTop(17);
        TextureRegion blackTexture = new TextureRegion(new Texture(Gdx.files.internal("Hud/energy_mask.png")), 183, 28);
        energyMax = blackTexture.getRegionWidth();
        energyMask = new Image(blackTexture);
        dataTable.add(energyMask).padLeft(12);
        dataTable.add(lifeLabel).padLeft(30);
        dataTable.row().padTop(25);
        dataTable.add(scoreLabel).padLeft(215);

        stage.addActor(dataTable);
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));

        addEnergy(5);
    }

    public static void addEnergy(int value) {
        energy += value;
        if (energy > 100) {
            energy = 100;
        }

        int width = (energyMax * energy / 100);
        if (dataTable != null) {
            Cell cell = dataTable.getCell(energyMask);
            if (cell != null) {
                dataTable.getCell(energyMask).width(energyMax - width).padLeft(12 + width);
            }
        }
    }

    public static void addLife(int value) {
        life += value;
        lifeLabel.setText(life.toString());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
