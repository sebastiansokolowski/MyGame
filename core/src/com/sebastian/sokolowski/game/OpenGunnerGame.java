package com.sebastian.sokolowski.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sebastian.sokolowski.game.screens.PlayScreen;

public class OpenGunnerGame extends Game {
    public static final int V_WIDTH = 1280;
    public static final int V_HEIGHT = 720;
    public static final float PPM = 100;

    //Collision bits
    public static final short GROUND_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short PLAYER_HEAD_BIT = 4;
    public static final short PLAYER_SHOOT_BIT = 8;
    public static final short ENEMY_BIT = 16;
    public static final short ENEMY_SHOOT_BIT = 32;
    public static final short CLIMBER_BIT = 64;

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
