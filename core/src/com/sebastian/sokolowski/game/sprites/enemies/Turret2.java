package com.sebastian.sokolowski.game.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sebastian.sokolowski.game.OpenGunnerGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;

/**
 * Created by Sebastian Sokołowski on 04.07.17.
 */

public class Turret2 extends Enemy {
    public enum State {
        BASIC,
        SHOOTING,
        DEAD
    }

    private State currentState;
    private State previousState;

    private Animation basic;
    private Animation basicShoot;

    public Turret2(PlayScreen playScreen, float x, float y) {
        super(new TextureAtlas("Tiles/Enemies/Turret2/turret2.pack"), playScreen, x, y);
        currentState = State.BASIC;

        setBounds(0, 0, 50 / OpenGunnerGame.PPM, 50 / OpenGunnerGame.PPM);
        setRegion((TextureRegion) basic.getKeyFrame(stateTimer));
    }

    @Override
    public void loadTextures() {
        basic = loadAnimation("idle", 2, 50, 50);
        basicShoot = loadAnimation("shoot", 4, 50, 50);
    }

    @Override
    public void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 1000;

        body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(25 / OpenGunnerGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = OpenGunnerGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = OpenGunnerGame.GROUND_BIT |
                OpenGunnerGame.PLAYER_SHOOT_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public void update(float dt) {
        TextureRegion textureRegion = getFrame(dt);

        setBounds(body.getPosition().x - getWidth() / 2, body.getPosition().y - 50 / OpenGunnerGame.PPM / 2,
                textureRegion.getRegionWidth() / OpenGunnerGame.PPM, textureRegion.getRegionHeight() / OpenGunnerGame.PPM);
        setRegion(textureRegion);

        if (body.getPosition().y < 0) {
            currentState = State.DEAD;
        }

        super.update(dt);
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
        super.setDead();
    }

    private TextureRegion getFrame(float delta) {
        TextureRegion textureRegion;
        switch (currentState) {
            case SHOOTING:
                textureRegion = (TextureRegion) basicShoot.getKeyFrame(stateTimer, true);
                break;
            case BASIC:
            default:
                textureRegion = (TextureRegion) basic.getKeyFrame(stateTimer, true);
        }

        if(previousState != State.DEAD){
            if ((body.getLinearVelocity().x < 0 || !runningRight) && textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
                runningRight = false;
            } else if ((body.getLinearVelocity().x > 0 || runningRight) && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
                runningRight = true;
            }
        }

        stateTimer = stateTimer + delta;
        previousState = currentState;
        return textureRegion;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
