package com.sebastian.sokolowski.game.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sebastian.sokolowski.game.screens.PlayScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian Sokołowski on 04.07.17.
 */

public abstract class Enemy extends Sprite {
    public World world;
    public PlayScreen playScreen;

    public List<Bullet> bulletList;
    public Body body;
    public TextureAtlas textureAtlas;

    public boolean runningRight;
    public float stateTimer;

    public Enemy(TextureAtlas textureAtlas, PlayScreen playScreen, float x, float y) {
        this.textureAtlas = textureAtlas;
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        this.bulletList = new ArrayList<Bullet>();
        this.runningRight = true;
        this.stateTimer = 0;

        setPosition(x, y);
        loadTextures();
        defineBody();
    }

    public abstract void loadTextures();

    public abstract void defineBody();

    public abstract void update(float dt);

    public abstract void fire();

    public TextureRegion loadTexture(String name, int i, int width, int height) {
        TextureRegion textureRegion = textureAtlas.findRegion(name);
        return new TextureRegion(textureRegion, i * width, 0, width, height);
    }

    public Animation loadAnimation(String name, int count, int width, int height) {
        TextureRegion textureRegion = textureAtlas.findRegion(name);
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < count; i++) {
            frames.add(new TextureRegion(textureRegion, i * width, 0, width, height));
        }

        return new Animation(0.1f, frames);
    }
}
