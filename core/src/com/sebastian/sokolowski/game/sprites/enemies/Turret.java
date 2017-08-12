package com.sebastian.sokolowski.game.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sebastian.sokolowski.game.OpenGunnerGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;
import com.sebastian.sokolowski.game.sprites.Bullet;

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
    private State previousState;

    private TextureRegion basic;
    private TextureRegion basicShot;

    // Bullet
    boolean shotFirst = true;
    private TextureRegion bullet1;
    private TextureRegion bullet2;

    public Turret(PlayScreen playScreen, float x, float y) {
        super(new TextureAtlas("Tiles/Enemies/Turret/turret.pack"), playScreen, x, y);
        currentState = State.BASIC;

        setBounds(0, 0, 50 / OpenGunnerGame.PPM, 50 / OpenGunnerGame.PPM);
        setRegion(basic);
    }

    @Override
    public void loadTextures() {
        basic = loadTexture("basic", 0, 50, 50);
        basicShot = loadTexture("basic", 1, 50, 50);
        bullet1 = loadTexture("bullet1", 0, 12, 12);
        bullet2 = loadTexture("bullet2", 0, 12, 12);
    }

    @Override
    public void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + basic.getRegionWidth() / 2 / OpenGunnerGame.PPM, getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(25 / OpenGunnerGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = OpenGunnerGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = OpenGunnerGame.GROUND_BIT |
                OpenGunnerGame.PLAYER_SHOOT_BIT;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
    }

    public void update(float delta) {
        if (body == null) {
            return;
        }

        TextureRegion textureRegion = getFrame(delta);

        if (currentState == State.DEAD) {
            if (!destroyed && bulletList.size == 0) {
                world.destroyBody(body);
                body = null;
                destroyed = true;
                return;
            }
        } else {
            if (body.getPosition().y < 0) {
                currentState = State.DEAD;
            }

            fireTimer += delta;
            if (fireTimer > fireDelay) {
                fireTimer = 0;
                fire();
            }
        }

        setBounds(body.getPosition().x - getWidth() / 2, body.getPosition().y - 50 / OpenGunnerGame.PPM / 2,
                textureRegion.getRegionWidth() / OpenGunnerGame.PPM, textureRegion.getRegionHeight() / OpenGunnerGame.PPM);
        setRegion(textureRegion);

        if (body.getPosition().y < 0) {
            currentState = State.DEAD;
        }

        super.update(delta);
    }

    @Override
    public void fire() {
        if (body != null && body.isActive()) {
            shotFirst = !shotFirst;
            bulletList.add(new TurretBullet(playScreen, body.getPosition().x, body.getPosition().y, runningRight,
                    shotFirst ? bullet1 : bullet2));
        }
    }

    @Override
    public void setDeadState() {
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

        if (previousState != State.DEAD) {
            if ((body.getLinearVelocity().x < 0 || !runningRight) && textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
                runningRight = false;
            } else if ((body.getLinearVelocity().x > 0 || runningRight) && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
                runningRight = true;
            }
        }

        stateTimer = currentState == State.SHOOTING ? stateTimer + delta : 0;
        if (stateTimer > fireDelay) {
            currentState = State.BASIC;
        }
        previousState = currentState;
        return textureRegion;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    class TurretBullet extends Bullet {
        public TurretBullet(PlayScreen playScreen, float x, float y, boolean runningRight, TextureRegion textureRegion) {
            super(textureRegion,
                    playScreen, x, y, 2.5f, (new Vector2(playScreen.getPlayer().getX(), playScreen.getPlayer().getY())).sub(x, y).angle());
            defineBody();
        }

        @Override
        public void defineBody() {
            BodyDef bdef = new BodyDef();

            if (angle >= 0 && angle <= 90) {
                bdef.position.set(getX(), getY());
            } else {
                bdef.position.set(getX(), getY() - 10 / OpenGunnerGame.PPM);
            }

            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.bullet = true;

            body = world.createBody(bdef);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(3 / OpenGunnerGame.PPM);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.density = 0.000001f;
            fixtureDef.filter.categoryBits = OpenGunnerGame.ENEMY_SHOOT_BIT;
            fixtureDef.filter.maskBits = OpenGunnerGame.PLAYER_BIT;

            body.createFixture(fixtureDef).setUserData(this);
        }

    }
}
