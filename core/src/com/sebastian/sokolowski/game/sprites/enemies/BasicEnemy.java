package com.sebastian.sokolowski.game.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.sebastian.sokolowski.game.OpenGunnerGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;
import com.sebastian.sokolowski.game.sprites.Bullet;

/**
 * Created by Sebastian Sokołowski on 02.07.17.
 */

public class BasicEnemy extends Enemy {

    public enum State {
        STANDING,
        CROUCHING,
        JUMPING,
        FALLING,
        GUN_0,
        DAMAGE,
        DEAD
    }

    public State currentState;
    public State previousState;
    private Array<BasicEnemyBullet> bulletList;

    private TextureRegion playerStandGun0;
    private TextureRegion playerJump;
    private TextureRegion playerDamage;
    private TextureRegion playerCrouch;
    private Animation playerRunGun0;

    private float fireTimer = 0;
    private float fireDelay = 1f;
    private float deadDelay = 0.5f;

    public BasicEnemy(PlayScreen playScreen, Float x, Float y) {
        super(new TextureAtlas("Tiles/Enemies/Enemy/enemy.pack"), playScreen, x, y);
        currentState = State.GUN_0;
        previousState = State.GUN_0;
        bulletList = new Array<BasicEnemyBullet>();

        setBounds(0, 0, 50 / OpenGunnerGame.PPM, 50 / OpenGunnerGame.PPM);
        setRegion(playerStandGun0);
    }

    @Override
    public void loadTextures() {
        //basic
        playerStandGun0 = loadTexture("basic", 0, 50, 50);
        playerCrouch = loadTexture("basic", 1, 50, 50);
        playerJump = loadTexture("basic", 2, 50, 50);

        //damage
        playerDamage = loadTexture("damage", 0, 50, 50);

        //run
        playerRunGun0 = loadAnimation("run", 8, 50, 50);
    }

    @Override
    public void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(25 / OpenGunnerGame.PPM);

        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void update(float delta) {
        TextureRegion textureRegion = getFrame(delta);

        if (currentState == State.DEAD) {
            if (stateTimer > deadDelay && !destroy && bulletList.size == 0) {
                world.destroyBody(body);
                destroy = true;
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

        for (BasicEnemyBullet ball : bulletList) {
            ball.update(delta);
            if (ball.isDestroyed()) {
                bulletList.removeValue(ball, true);
            }
        }
    }

    @Override
    public void fire() {
        bulletList.add(new BasicEnemyBullet(playScreen, body.getPosition().x, body.getPosition().y, runningRight));
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
        currentState = getState();

        TextureRegion textureRegion;
        switch (currentState) {
            case FALLING:
            case JUMPING:
                textureRegion = playerJump;
                break;
            case DEAD:
            case DAMAGE:
                textureRegion = playerDamage;
                break;
            case CROUCHING:
                textureRegion = playerCrouch;
                break;
            case GUN_0:
            default:
                if (body.getLinearVelocity().x != 0) {
                    textureRegion = (TextureRegion) playerRunGun0.getKeyFrame(stateTimer, true);
                } else {
                    textureRegion = playerStandGun0;
                }
        }

        if ((body.getLinearVelocity().x < 0 || !runningRight) && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return textureRegion;
    }

    @Override
    public void draw(Batch batch) {
        if (!destroy) {
            super.draw(batch);
        }

        for (BasicEnemyBullet ball : bulletList) {
            ball.draw(batch);
        }
    }

    private State getState() {
        if (currentState == State.DEAD) {
            return State.DEAD;
        }

        if (body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return State.JUMPING;
        } else {
            return State.STANDING;
        }
    }

    class BasicEnemyBullet extends Bullet {
        public BasicEnemyBullet(PlayScreen playScreen, float x, float y, boolean runningRight) {
            super(new TextureRegion(new Texture(Gdx.files.internal("Tiles/Player/bullet.png")), 36, 18),
                    playScreen, x, y, runningRight ? 0 : 180);
            defineBody();
        }

        @Override
        public void defineBody() {
            BodyDef bdef = new BodyDef();

            if (angle >= 0 && angle <= 90) {
                bdef.position.set(getX() + 35 / OpenGunnerGame.PPM, getY() + 7 / OpenGunnerGame.PPM);
            } else {
                bdef.position.set(getX() - 35 / OpenGunnerGame.PPM, getY() + 7 / OpenGunnerGame.PPM);
            }

            bdef.type = BodyDef.BodyType.KinematicBody;

            body = world.createBody(bdef);

            FixtureDef fixtureDef = new FixtureDef();
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(3 / OpenGunnerGame.PPM);

            fixtureDef.shape = circleShape;

            body.createFixture(fixtureDef).setUserData(this);
        }

    }
}
