package com.sebastian.sokolowski.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
 * Created by Sebastian Sokołowski on 06.04.17.
 */

public class Player extends Sprite {
    public enum State {DEAD, CROUCHING, FALLING, JUMPING, STANDING, RUNNING}

    public State currentState;
    public State previousState;
    public World world;
    public Body body;

    private TextureRegion playerStand;
    private Animation playerRun;
    private TextureRegion playerJump;
    private TextureRegion playerCrouching;

    private float stateTimer;
    private boolean runningRight;

    public Player(World world, PlayScreen playScreen) {
        super(playScreen.getTextureAtlas().findRegion("basic"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 3; i < 7; i++) {
            frames.add(new TextureRegion(getTexture(), i * 50, 0, 50, 50));
        }
        playerRun = new Animation(0.1f, frames);
        playerJump = new TextureRegion(getTexture(), 2 * 50, 0, 50, 50);
        playerCrouching = new TextureRegion(getTexture(), 50, 0, 50, 50);
        playerStand = new TextureRegion(getTexture(), 0, 0, 50, 50);

        definePlayer();
        setBounds(0, 0, 50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
        setRegion(playerStand);
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
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));

        if (body.getPosition().y < 0) {
            currentState = State.DEAD;
        }
    }

    private TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion textureRegion;
        switch (currentState) {
            case JUMPING:
                textureRegion = playerJump;
                break;
            case RUNNING:
                textureRegion = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
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
