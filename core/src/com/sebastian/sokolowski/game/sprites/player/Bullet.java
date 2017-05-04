package com.sebastian.sokolowski.game.sprites.player;

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
 * Created by Sebastian Sokołowski on 29.04.17.
 */

public class Bullet extends Sprite {
    private final PlayScreen playScreen;
    private final World world;
    private final TextureRegion textureRegion;
    private final Player.State playerState;
    private final boolean runningRight;
    private Vector2 bulletVector = new Vector2(10, 10);
    private Body body;

    private boolean destroyed;
    float stateTime;
    private boolean setToDestroy;

    public Bullet(PlayScreen playScreen, float x, float y, Player.State playerState, boolean runningRight) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        this.playerState = playerState;
        this.runningRight = runningRight;

        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Tiles/Player/bullet.png")), 36, 18);
        setRegion(textureRegion);
        float angle = getAngle(playerState, runningRight);
        bulletVector.setAngle(angle);
        setBounds(x, y, 36 / MyGdxGame.PPM, 18 / MyGdxGame.PPM);
        setOriginCenter();
        setRotation(angle);
        defineBullet(playerState, runningRight);
    }

    private float getAngle(Player.State playerState, boolean runningRight) {
        switch (playerState) {
            case GUN_DOWN_90:
                return 270;
            case GUN_UP_90:
                return 90;
            case GUN_UP_30:
                if (runningRight) {
                    return 30;
                } else {
                    return 150;
                }
            case GUN_UP_45:
                if (runningRight) {
                    return 45;
                } else {
                    return 135;
                }
            case GUN_DOWN_30:
                if (runningRight) {
                    return 330;
                } else {
                    return 210;
                }
            case GUN_DOWN_45:
                if (runningRight) {
                    return 315;
                } else {
                    return 225;
                }
            default:
                if (runningRight) {
                    return 0;
                } else {
                    return 180;
                }
        }
    }

    public void defineBullet(Player.State playerState, boolean runningRight) {
        BodyDef bdef = new BodyDef();

        switch (playerState) {
            case GUN_DOWN_90:
                if (runningRight) {
                    bdef.position.set(getX() - 5 / MyGdxGame.PPM, getY() - 50 / MyGdxGame.PPM);
                } else {
                    bdef.position.set(getX() + 5 / MyGdxGame.PPM, getY() - 50 / MyGdxGame.PPM);
                }
                break;
            case GUN_UP_90:
                if (runningRight) {
                    bdef.position.set(getX() - 5 / MyGdxGame.PPM, getY() + 50 / MyGdxGame.PPM);
                } else {
                    bdef.position.set(getX() + 5 / MyGdxGame.PPM, getY() + 50 / MyGdxGame.PPM);
                }
                break;
            case GUN_UP_30:
                if (runningRight) {
                    bdef.position.set(getX() + 38 / MyGdxGame.PPM, getY() + 28 / MyGdxGame.PPM);
                } else {
                    bdef.position.set(getX() - 38 / MyGdxGame.PPM, getY() + 28 / MyGdxGame.PPM);
                }
                break;
            case GUN_UP_45:
                if (runningRight) {
                    bdef.position.set(getX() + 30 / MyGdxGame.PPM, getY() + 40 / MyGdxGame.PPM);
                } else {
                    bdef.position.set(getX() - 30 / MyGdxGame.PPM, getY() + 40 / MyGdxGame.PPM);
                }
                break;
            case GUN_DOWN_30:
                if (runningRight) {
                    bdef.position.set(getX() + 38 / MyGdxGame.PPM, getY() - 25 / MyGdxGame.PPM);
                } else {
                    bdef.position.set(getX() - 38 / MyGdxGame.PPM, getY() - 25 / MyGdxGame.PPM);
                }
                break;
            case GUN_DOWN_45:
                if (runningRight) {
                    bdef.position.set(getX() + 30 / MyGdxGame.PPM, getY() - 30 / MyGdxGame.PPM);
                } else {
                    bdef.position.set(getX() - 30 / MyGdxGame.PPM, getY() - 30 / MyGdxGame.PPM);
                }
                break;
            default:
                if (runningRight) {
                    bdef.position.set(getX() + 35 / MyGdxGame.PPM, getY() + 7 / MyGdxGame.PPM);
                } else {
                    bdef.position.set(getX() - 35 / MyGdxGame.PPM, getY() + 7 / MyGdxGame.PPM);
                }
                break;
        }
//
//
//        if (angle >= 0 && angle <= 90 ||
//                angle >= 270 && angle <= 360) {
//            // right
//            bdef.position.set(getX() + 25 / MyGdxGame.PPM, getY() + 50 / MyGdxGame.PPM);
//        } else if (angle >= 75 && angle <= 105) {
//            // up
//            bdef.position.set(getX(), getY() + 25 / MyGdxGame.PPM);
//        } else if (angle >= 255 && angle <= 285) {
//            // down
//            bdef.position.set(getX(), getY() - 25 / MyGdxGame.PPM);
//        } else {
//            // left
//            bdef.position.set(getX() - 25 / MyGdxGame.PPM, getY() + 50 / MyGdxGame.PPM);
//        }

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
