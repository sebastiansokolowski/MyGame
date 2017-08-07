package com.sebastian.sokolowski.game.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sebastian.sokolowski.game.screens.PlayScreen;
import com.sebastian.sokolowski.game.sprites.Bullet;
import com.sebastian.sokolowski.game.sprites.player.Player;


/**
 * Created by Sebastian Sokołowski on 04.07.17.
 */

public abstract class Enemy extends Sprite {
    World world;
    PlayScreen playScreen;
    Player player;

    public Array<Bullet> bulletList;
    public Body body;
    public TextureAtlas textureAtlas;

    public boolean runningRight;
    public float stateTimer;

    public boolean destroy;

    public Enemy(TextureAtlas textureAtlas, PlayScreen playScreen, float x, float y) {
        this.textureAtlas = textureAtlas;
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        this.player = playScreen.getPlayer();
        this.bulletList = new Array<Bullet>();
        this.runningRight = true;
        this.destroy = false;
        this.stateTimer = 0;

        setPosition(x, y);
        loadTextures();
        defineBody();
        body.setActive(false);
    }

    public abstract void loadTextures();

    public abstract void defineBody();

    public abstract void fire();

    public abstract void setDeadState();

    public abstract boolean isDead();

    public void setDead() {
        setDeadState();
    }

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

    public void update(float delta) {
        for (Bullet bullet : bulletList) {
            bullet.update(delta);
            if (bullet.isDestroyed()) {
                bulletList.removeValue(bullet, true);
            }
        }

        if (destroy) {
            return;
        }

        if (player.getX() >= getX()) {
            runningRight = true;
        } else {
            runningRight = false;
        }
    }

    @Override
    public void draw(Batch batch) {
        for (Bullet ball : bulletList) {
            ball.draw(batch);
        }
        super.draw(batch);
    }
}
