package com.sebastian.sokolowski.game.sprites.player;

import com.badlogic.gdx.graphics.g2d.Animation;
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

import java.util.List;

/**
 * Created by Sebastian Sokołowski on 06.04.17.
 */

public class Player extends Sprite {
    public enum State {DEAD, CROUCHING, FALLING, JUMPING, STANDING, RUNNING}

    private final TextureAtlas textureAtlas = new TextureAtlas("Tiles/Player/player.pack");

    public State currentState;
    public State previousState;
    public World world;
    public Body body;
    private PlayScreen playScreen;
    private float knobPercentX;
    private float knobPercentY;

    private TextureRegion playerJump;
    private TextureRegion playerCrouching;
    private TextureRegion playerStand;
    private Animation playerCrawl;
    private TextureRegion playerDamage;
    private Animation playerRunGun0;
    private Animation playerRunGunDown30;
    private Animation playerRunGunDown60;
    private Animation playerRunGunUp30;
    private Animation playerRunGunUp60;

    private List<Bullet> bulletList;

    private float stateTimer;
    private boolean runningRight;

    public Player(World world, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        loadTextures();

        definePlayer();
        setBounds(0, 0, 50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
        setRegion(playerStand);
    }

    private void loadTextures() {
        //basic
        playerJump = loadTexture("basic", 2, 50, 50);
        playerCrouching = loadTexture("basic", 1, 50, 50);
        playerStand = loadTexture("basic", 0, 50, 50);

        //crawl
        playerCrawl = loadAnimation("crawl", 70, 50);

        //damage
        playerDamage = loadTexture("damage", 0, 50, 50);

        //run gun 0
        playerRunGun0 = loadAnimation("run", 50, 50);

        //run gun -30
        playerRunGunDown30 = loadAnimation("run_gun_30_down", 50, 57);

        //run gun -60
        playerRunGunDown60 = loadAnimation("run_gun_60_down", 50, 57);

        //run gun +30
        playerRunGunUp30 = loadAnimation("run_gun_30_up", 50, 57);

        //run gun +60
        playerRunGunUp60 = loadAnimation("run_gun_60_up", 50, 57);
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
        bodyDef.position.set((MyGdxGame.V_WIDTH / 2) / MyGdxGame.PPM, 210 / MyGdxGame.PPM);
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
    }

    public void setKnobPercent(float knobPercentX, float knobPercentY) {
        this.knobPercentX = knobPercentX;
        this.knobPercentY = knobPercentY;
    }

    private TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion textureRegion;
        switch (currentState) {
            case JUMPING:
                textureRegion = playerJump;
                break;
            case RUNNING:
                if (knobPercentY > 0.6f) {
                    textureRegion = (TextureRegion) playerRunGunUp60.getKeyFrame(stateTimer, true);
                } else if (knobPercentY > 0.3f) {
                    textureRegion = (TextureRegion) playerRunGunUp30.getKeyFrame(stateTimer, true);
                } else if (knobPercentY > 0f) {
                    textureRegion = (TextureRegion) playerRunGun0.getKeyFrame(stateTimer, true);
                } else if (knobPercentY > -0.6f) {
                    textureRegion = (TextureRegion) playerRunGunDown30.getKeyFrame(stateTimer, true);
                } else if (knobPercentY > -0.9f) {
                    textureRegion = (TextureRegion) playerRunGunDown60.getKeyFrame(stateTimer, true);
                } else {
                    textureRegion = (TextureRegion) playerRunGun0.getKeyFrame(stateTimer, true);
                }
                break;
            case CROUCHING:
                //TODO:
            case FALLING:
            default:
                textureRegion = playerStand;
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

    private State getState() {
        if (body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }
}
