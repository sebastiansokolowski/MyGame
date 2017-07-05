package com.sebastian.sokolowski.game.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;

/**
 * Created by Sebastian Sokołowski on 04.07.17.
 */

public class Turret extends Enemy {
    public enum State {
        BASIC,
        SHOOTING,
        DEAD
    }

    private State currentState;

    private TextureRegion basic;
    private TextureRegion basicShot;

    private long shootingStateTime = 1000;

    public Turret(PlayScreen playScreen, float x, float y) {
        super(new TextureAtlas("Tiles/Enemies/Turret/turret.pack"), playScreen, x, y);
        currentState = State.BASIC;

        setBounds(0, 0, 50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
        setRegion(basic);
    }

    @Override
    public void loadTextures() {
        basic = loadTexture("basic", 0, 50, 50);
        basicShot = loadTexture("basic", 1, 50, 50);
    }

    @Override
    public void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 1000;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(25 / MyGdxGame.PPM);

        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef);
        body.setUserData(this);
    }

    @Override
    public void update(float dt) {
        TextureRegion textureRegion = getFrame(dt);

        setBounds(body.getPosition().x - getWidth() / 2, body.getPosition().y - 50 / MyGdxGame.PPM / 2,
                textureRegion.getRegionWidth() / MyGdxGame.PPM, textureRegion.getRegionHeight() / MyGdxGame.PPM);
        setRegion(textureRegion);

        if (body.getPosition().y < 0) {
            currentState = State.DEAD;
        }
    }

    @Override
    public void fire() {

    }

    @Override
    public boolean isDead() {
        if (currentState == State.DEAD) {
            return true;
        }
        return false;
    }

    @Override
    public void setDead() {
        currentState = State.DEAD;
    }

    private TextureRegion getFrame(float delta) {
        TextureRegion textureRegion;
        switch (currentState) {
            case SHOOTING:
                textureRegion = basicShot;
                break;
            case BASIC:
            default:
                textureRegion = basic;

        }

        if ((body.getLinearVelocity().x < 0 || !runningRight) && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == State.SHOOTING ? stateTimer + delta : 0;
        if (stateTimer > shootingStateTime) {
            currentState = State.BASIC;
        }
        return textureRegion;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
