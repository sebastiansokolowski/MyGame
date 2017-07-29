package com.sebastian.sokolowski.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sebastian.sokolowski.game.screens.PlayScreen;

public class OpenGunnerGame extends Game {
    public static final int V_WIDTH = 1280;
    public static final int V_HEIGHT = 720;
    public static final float PPM = 100;

    public static SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
