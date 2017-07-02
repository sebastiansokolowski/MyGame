package com.sebastian.sokolowski.game.sprites.enemies.basic;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;

/**
 * Created by Sebastian Sokołowski on 02.07.17.
 */

public class BasicEnemy extends Sprite {

    public enum State {
        STANDING,
        CROUCHING,
        JUMPING,
        FALLING,
        GUN_0,
        DAMAGE,
        DEAD
    }

    public World world;
    private PlayScreen playScreen;

    private final TextureAtlas textureAtlas = new TextureAtlas("Tiles/Enemies/Enemy/enemy.pack");
    public Body body;
    public State currentState;
    public State previousState;
    private boolean runningRight;
    private Array<BasicEnemyBullet> bulletList;

    private TextureRegion playerStandGun0;
    private TextureRegion playerJump;
    private TextureRegion playerDamage;
    private TextureRegion playerCrouch;
    private Animation playerRunGun0;

    private float stateTimer;

    private long fireLastTime = System.currentTimeMillis();
    private long fireDelay = 2000;

    public BasicEnemy(World world, Float x, Float y, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;
        currentState = State.GUN_0;
        previousState = State.GUN_0;
        runningRight = true;
        stateTimer = 0;
        bulletList = new Array<BasicEnemyBullet>();

        setPosition(x, y);
        loadTextures();

        definePlayer();
        setBounds(0, 0, 50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
        setRegion(playerStandGun0);
    }

    private void loadTextures() {
        //basic
        playerStandGun0 = loadTexture("basic", 0, 50, 50);
        playerCrouch = loadTexture("basic", 1, 50, 50);
        playerJump = loadTexture("basic", 2, 50, 50);

        //damage
        playerDamage = loadTexture("damage", 0, 50, 50);

        //run
        playerRunGun0 = loadAnimation("run", 50, 50);
    }

    private TextureRegion loadTexture(String name, int i, int width, int height) {
        TextureRegion textureRegion = textureAtlas.findRegion(name);
        return new TextureRegion(textureRegion, i * width, 0, width, height);
    }

    private Animation loadAnimation(String name, int width, int height) {
        TextureRegion textureRegion = textureAtlas.findRegion(name);
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(textureRegion, i * width, 0, width, height));
        }

        return new Animation(0.1f, frames);
    }

    private void definePlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(25 / MyGdxGame.PPM);

        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef);
    }

    public void update(float delta) {
        TextureRegion textureRegion = getFrame(delta);

        setBounds(body.getPosition().x - getWidth() / 2, body.getPosition().y - 50 / MyGdxGame.PPM / 2,
                textureRegion.getRegionWidth() / MyGdxGame.PPM, textureRegion.getRegionHeight() / MyGdxGame.PPM);
        setRegion(textureRegion);

        if (body.getPosition().y < 0) {
            currentState = State.DEAD;
        }

        for (BasicEnemyBullet ball : bulletList) {
            ball.update(delta);
            if (ball.isDestroyed()) {
                bulletList.removeValue(ball, true);
            }
        }

        if (fireLastTime + fireDelay < System.currentTimeMillis()) {
            fire();
            fireLastTime = System.currentTimeMillis();
        }
    }

    public void fire() {
        bulletList.add(new BasicEnemyBullet(playScreen, body.getPosition().x, body.getPosition().y, runningRight));
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
        super.draw(batch);
        for (BasicEnemyBullet ball : bulletList) {
            ball.draw(batch);
        }
    }

    private State getState() {
        if (body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return State.JUMPING;
        } else {
            return State.STANDING;
        }
    }
}
