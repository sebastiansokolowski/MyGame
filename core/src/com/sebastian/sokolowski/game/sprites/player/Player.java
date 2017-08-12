package com.sebastian.sokolowski.game.sprites.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sebastian.sokolowski.game.OpenGunnerGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;

/**
 * Created by Sebastian Sokołowski on 06.04.17.
 */

public class Player extends Sprite {
    public enum State {
        DEAD, CROUCHING, FALLING, JUMPING,
        GUN_0,
        GUN_UP_45, GUN_UP_30,
        GUN_DOWN_45, GUN_DOWN_30,
        GUN_UP_90, GUN_DOWN_90,
    }

    //controll
    private static float jumpHeight = 5;

    public boolean runningRight;

    private final TextureAtlas textureAtlas = new TextureAtlas("Tiles/Player/player.pack");

    public State currentState;
    public State previousState;
    public World world;
    public Body body;
    private Fixture fixture;
    private PlayScreen playScreen;
    private Vector2 knobVector = new Vector2(0, 0);

    private TextureRegion playerJump;
    private TextureRegion playerCrouch;
    private Animation playerCrawl;
    private TextureRegion playerDamage;
    private Animation playerRunGun0;
    private Animation playerRunGunDown30;
    private Animation playerRunGunDown60;
    private Animation playerRunGunUp30;
    private Animation playerRunGunUp60;
    private TextureRegion playerStandGun0;
    private TextureRegion playerStandGunDown30;
    private TextureRegion playerStandGunDown60;
    private TextureRegion playerStandGunDown90;
    private TextureRegion playerStandGunUp90;
    private TextureRegion playerStandGunUp60;
    private TextureRegion playerStandGunUp30;

    private Array<PlayerBullet> bulletList;

    private float stateTimer;

    public Player(World world, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;
        currentState = State.GUN_0;
        previousState = State.GUN_0;
        stateTimer = 0;
        runningRight = true;
        bulletList = new Array<PlayerBullet>();

        loadTextures();

        definePlayer();
        setBounds(0, 0, 50 / OpenGunnerGame.PPM, 50 / OpenGunnerGame.PPM);
        setRegion(playerStandGun0);
    }

    private void loadTextures() {
        //basic
        playerJump = loadTexture("basic", 2, 50, 50);
        playerCrouch = loadTexture("basic", 1, 50, 50);
        playerStandGun0 = loadTexture("basic", 0, 50, 50);

        //crawl
        playerCrawl = loadAnimation("crawl", 70, 50);

        //damage
        playerDamage = loadTexture("damage", 0, 50, 50);

        //run gun
        playerRunGun0 = loadAnimation("run", 50, 50);
        playerRunGunDown30 = loadAnimation("run_gun_30_down", 50, 57);
        playerRunGunDown60 = loadAnimation("run_gun_60_down", 50, 57);
        playerRunGunUp30 = loadAnimation("run_gun_30_up", 50, 57);
        playerRunGunUp60 = loadAnimation("run_gun_60_up", 50, 57);

        //stand gun
        playerStandGunUp90 = loadTexture("stand_gun_90_up", 0, 50, 57);
        playerStandGunUp60 = loadTexture("stand_gun_60_up", 0, 50, 57);
        playerStandGunUp30 = loadTexture("stand_gun_30_up", 0, 50, 57);
        playerStandGunDown30 = loadTexture("stand_gun_30_down", 0, 50, 57);
        playerStandGunDown60 = loadTexture("stand_gun_60_down", 0, 50, 57);
        playerStandGunDown90 = loadTexture("stand_gun_90_down", 0, 50, 57);
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
        bodyDef.position.set((OpenGunnerGame.V_WIDTH / 2) / OpenGunnerGame.PPM, 210 / OpenGunnerGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(25 / OpenGunnerGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = OpenGunnerGame.PLAYER_BIT;
        fixtureDef.filter.maskBits = OpenGunnerGame.GROUND_BIT |
                OpenGunnerGame.ENEMY_SHOOT_BIT;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
    }

    public void jump() {
        if (body.getLinearVelocity().y == 0) {
            if (currentState == State.CLIMB) {
                setCurrentState(State.FALLING);
            } else {
                body.applyLinearImpulse(new Vector2(0, jumpHeight), body.getWorldCenter(), true);
            }
        }
    }

    private void setFilterMask() {
        Filter filter = new Filter();
        filter.categoryBits = OpenGunnerGame.PLAYER_BIT;

        State state = getState();
        switch (state) {
            case JUMPING:
                filter.maskBits = OpenGunnerGame.ENEMY_SHOOT_BIT;
                break;
            case FALLING:
            default:
                filter.maskBits = OpenGunnerGame.GROUND_BIT |
                        OpenGunnerGame.ENEMY_SHOOT_BIT;
        }
        fixture.setFilterData(filter);
    }

    public void update(float delta) {
        TextureRegion textureRegion = getFrame(delta);

        setBounds(body.getPosition().x - getWidth() / 2, body.getPosition().y - 50 / OpenGunnerGame.PPM / 2,
                textureRegion.getRegionWidth() / OpenGunnerGame.PPM, textureRegion.getRegionHeight() / OpenGunnerGame.PPM);
        setRegion(textureRegion);

        if (body.getPosition().y < 0) {
            currentState = State.DEAD;
        }

        for (PlayerBullet ball : bulletList) {
            ball.update(delta);
            if (ball.isDestroyed()) {
                bulletList.removeValue(ball, true);
            }
        }

        setFilterMask();
    }

    public void fire() {
        bulletList.add(new PlayerBullet(playScreen, body.getPosition().x, body.getPosition().y, currentState, runningRight));
    }

    public void setKnobVector(Vector2 vector2) {
        this.knobVector = vector2;
    }

    private TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion textureRegion;
        switch (currentState) {
            case JUMPING:
                textureRegion = playerJump;
                break;
            case DEAD:
            case CROUCHING:
            case FALLING:
            case GUN_0:
                if (body.getLinearVelocity().x != 0) {
                    textureRegion = (TextureRegion) playerRunGun0.getKeyFrame(stateTimer, true);
                } else {
                    textureRegion = playerStandGun0;
                }
                break;
            case GUN_UP_30:
                if (body.getLinearVelocity().x != 0) {
                    textureRegion = (TextureRegion) playerRunGunUp30.getKeyFrame(stateTimer, true);
                } else {
                    textureRegion = playerStandGunUp30;
                }
                break;
            case GUN_UP_45:
                if (body.getLinearVelocity().x != 0) {
                    textureRegion = (TextureRegion) playerRunGunUp60.getKeyFrame(stateTimer, true);
                } else {
                    textureRegion = playerStandGunUp60;
                }
                break;
            case GUN_DOWN_30:
                if (body.getLinearVelocity().x != 0) {
                    textureRegion = (TextureRegion) playerRunGunDown30.getKeyFrame(stateTimer, true);
                } else {
                    textureRegion = playerStandGunDown30;
                }
                break;
            case GUN_DOWN_45:
                if (body.getLinearVelocity().x != 0) {
                    textureRegion = (TextureRegion) playerRunGunDown60.getKeyFrame(stateTimer, true);
                } else {
                    textureRegion = playerStandGunDown60;
                }
                break;
            case GUN_UP_90:
                textureRegion = playerStandGunUp90;
                break;
            case GUN_DOWN_90:
                textureRegion = playerStandGunDown90;
                break;
            default:
                textureRegion = playerStandGun0;
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
        for (PlayerBullet ball : bulletList) {
            ball.draw(batch);
        }
    }

    public void setDead() {
        currentState = State.DEAD;
    }

    private State getState() {
        if (currentState == State.DEAD) {
            return State.DEAD;
        }

        float angle = knobVector.angle();

        if (body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (angle > 80 && angle < 100) {
            return State.GUN_UP_90;
        } else if (angle > 260 && angle < 280) {
            return State.GUN_DOWN_90;
        } else if (knobVector.x == 0 && knobVector.y == 0) {
            return State.GUN_0;
        } else {
            boolean down = false;
            if (angle >= 180) {
                angle -= 180;
                down = true;
            }

            if (angle >= 0 && angle < 15 ||
                    angle > 165 && angle <= 180) {
                return State.GUN_0;
            } else if (angle >= 15 && angle < 45 ||
                    angle > 135 && angle <= 165) {
                if (down) {
                    return State.GUN_DOWN_30;
                } else {
                    return State.GUN_UP_30;
                }
            } else if (angle >= 45 && angle < 75 ||
                    angle > 105 && angle <= 135) {
                if (down) {
                    return State.GUN_DOWN_45;
                } else {
                    return State.GUN_UP_45;
                }
            } else {
                return currentState;
            }
        }
    }
}
