package com.sebastian.sokolowski.game.sprites.enemies.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;

/**
 * Created by Sebastian Sokołowski on 02.07.17.
 */

public class BasicEnemyBullet extends Sprite {
    private final PlayScreen playScreen;
    private final World world;
    private final TextureRegion textureRegion;
    private final boolean runningRight;
    private Vector2 bulletVector = new Vector2(10, 10);
    private Body body;

    private boolean destroyed;
    float stateTime;
    private boolean setToDestroy;

    public BasicEnemyBullet(PlayScreen playScreen, float x, float y, boolean runningRight) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        this.runningRight = runningRight;

        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Tiles/Player/bullet.png")), 36, 18);
        setRegion(textureRegion);
        float angle = getAngle(runningRight);
        bulletVector.setAngle(angle);
        setRegion(textureRegion);
        setBounds(x, y, 36 / MyGdxGame.PPM, 18 / MyGdxGame.PPM);
        setOriginCenter();
        setRotation(angle);
        defineBullet(runningRight);
    }

    private float getAngle(boolean runningRight) {
        if (runningRight) {
            return 0;
        } else {
            return 180;
        }
    }

    public void defineBullet(boolean runningRight) {
        BodyDef bdef = new BodyDef();

        if (runningRight) {
            bdef.position.set(getX() + 35 / MyGdxGame.PPM, getY() + 7 / MyGdxGame.PPM);
        } else {
            bdef.position.set(getX() - 35 / MyGdxGame.PPM, getY() + 7 / MyGdxGame.PPM);
        }

        bdef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bdef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(3 / MyGdxGame.PPM);

        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef);
    }

    public void update(float delta) {
        if (getY() * MyGdxGame.PPM > MyGdxGame.V_HEIGHT || getY() < 0 || stateTime > 5) {
            destroyed = true;
            world.destroyBody(body);
        }

        if (destroyed) {
            return;
        }

        stateTime += delta;

        setCenter(body.getPosition().x, body.getPosition().y);
        body.setLinearVelocity(bulletVector);
    }

    public void setToDestroy() {
        setToDestroy = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
