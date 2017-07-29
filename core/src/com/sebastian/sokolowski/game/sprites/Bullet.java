package com.sebastian.sokolowski.game.sprites;

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
    public static final String TAG = Bullet.class.getSimpleName();

    public final PlayScreen playScreen;
    public final World world;

    public final Vector2 bulletVector;
    public Body body;

    public float angle;
    public float stateTime;
    boolean setToDestroy;
    boolean destroyed;

    public Bullet(TextureRegion textureRegion, PlayScreen playScreen, float x, float y, float angle) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        this.angle = angle;
        this.stateTime = 0;
        this.setToDestroy = false;
        this.destroyed = false;
        bulletVector = new Vector2(10, 10);

        setRegion(textureRegion);

        setBounds(x, y, textureRegion.getRegionWidth() / MyGdxGame.PPM, textureRegion.getRegionHeight() / MyGdxGame.PPM);

        bulletVector.setAngle(angle);
        setOriginCenter();
        setRotation(angle);
    }

    public abstract void defineBody();

    public void update(float dt) {
        if (setToDestroy) {
            if (!destroyed) {
                world.destroyBody(body);
                destroyed = true;
            }
        } else {
            stateTime += dt;
            if (getY() * MyGdxGame.PPM > MyGdxGame.V_HEIGHT || getY() < 0 || stateTime > 5) {
                setToDestroy = true;
            }

            setCenter(body.getPosition().x, body.getPosition().y);
            body.setLinearVelocity(bulletVector);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setToDestroy() {
        this.setToDestroy = true;
    }

    public boolean isSetToDestroy() {
        return setToDestroy;
    }

    @Override
    public void draw(Batch batch) {
        if (!setToDestroy) {
            super.draw(batch);
        }
    }
}
