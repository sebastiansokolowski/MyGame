package com.sebastian.sokolowski.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sebastian.sokolowski.game.sprites.Player;

/**
 * Created by Sebastian Sokołowski on 27.04.17.
 */

public class Controller {
    private final Player player;
    private final FitViewport viewport;
    private final OrthographicCamera cam;
    private final Stage stage;

    private Touchpad touchpad;
    private static float blockSpeed = 3;
    private static float jumpHeight = 5;

    public Controller(final Player player) {
        this.player = player;

        cam = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, cam);
        stage = new Stage(viewport, MyGdxGame.batch);

        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("controller/touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("controller/touchKnob.png"));
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(15, 15, 200, 200);

        stage.addActor(touchpad);

        //add control buttons
        Image buttonA = new Image(new Texture("controller/buttonA.png"));
        buttonA.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (player.body.getLinearVelocity().y == 0) {
                    player.body.applyLinearImpulse(new Vector2(0, jumpHeight), player.body.getWorldCenter(), true);
                }
                return true;
            }
        });
        Image buttonB = new Image(new Texture("controller/buttonB.png"));
        buttonB.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //TODO:
                return true;
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.right().bottom();
        table.add(buttonA).size(buttonA.getWidth(), buttonA.getHeight()).pad(15);
        table.add(buttonB).size(buttonB.getWidth(), buttonB.getHeight()).pad(15);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    public void update() {
        if (player.currentState != Player.State.DEAD) {
            player.body.setLinearVelocity(new Vector2(touchpad.getKnobPercentX() * blockSpeed, player.body.getLinearVelocity().y));
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void draw() {
        stage.draw();
    }
}
