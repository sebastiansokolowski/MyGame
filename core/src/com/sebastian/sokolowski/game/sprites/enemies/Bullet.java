package com.sebastian.sokolowski.game.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;

/**
 * Created by Sebastian Sokołowski on 04.07.17.
 */

public abstract class Bullet extends Sprite {
    public final PlayScreen playScreen;
    public final World world;

    public final Vector2 bulletVector = new Vector2(10, 10);
    public Body body;

    public float angle;
    public float stateTime;
    boolean destroyed;

    public Bullet(TextureRegion textureRegion, PlayScreen playScreen, float x, float y, float angle) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        this.angle = angle;
        this.destroyed = false;

        setRegion(textureRegion);

        setBounds(x, y, textureRegion.getRegionWidth() / MyGdxGame.PPM, textureRegion.getRegionHeight() / MyGdxGame.PPM);

        bulletVector.setAngle(angle);
        setOriginCenter();
        setRotation(angle);

        defineBody();
    }

    public abstract void defineBody();

    public void update(float dt) {
        if (getY() * MyGdxGame.PPM > MyGdxGame.V_HEIGHT || getY() < 0 || stateTime > 5) {
            destroyed = true;
            world.destroyBody(body);
        }

        if (destroyed) {
            return;
        }

        stateTime += dt;

        setCenter(body.getPosition().x, body.getPosition().y);
        body.setLinearVelocity(bulletVector);
    }

    public void setDestroyed() {
        this.destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void draw(Batch batch) {
        if(!isDestroyed()){
            super.draw(batch);
        }
    }
}
