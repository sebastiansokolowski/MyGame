package com.sebastian.sokolowski.game.sprites.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;

/**
 * Created by Sebastian Sokołowski on 29.04.17.
 */

public class PlayerBullet extends com.sebastian.sokolowski.game.sprites.Bullet {
    private final Player.State playerState;
    private final boolean runningRight;

    public PlayerBullet(PlayScreen playScreen, float x, float y, Player.State playerState, boolean runningRight) {
        super(new TextureRegion(new Texture(Gdx.files.internal("Tiles/Player/bullet.png")), 36, 18),
                playScreen, x, y, getAngle(playerState, runningRight));
        this.playerState = playerState;
        this.runningRight = runningRight;

        defineBody();
    }

    private static float getAngle(Player.State playerState, boolean runningRight) {
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

    public void defineBody() {
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

        bdef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bdef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(3 / MyGdxGame.PPM);

        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef).setUserData(this);
    }
}
