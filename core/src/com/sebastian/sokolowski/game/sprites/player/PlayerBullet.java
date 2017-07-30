package com.sebastian.sokolowski.game.sprites.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sebastian.sokolowski.game.OpenGunnerGame;
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
                    bdef.position.set(getX() - 5 / OpenGunnerGame.PPM, getY() - 50 / OpenGunnerGame.PPM);
                } else {
                    bdef.position.set(getX() + 5 / OpenGunnerGame.PPM, getY() - 50 / OpenGunnerGame.PPM);
                }
                break;
            case GUN_UP_90:
                if (runningRight) {
                    bdef.position.set(getX() - 5 / OpenGunnerGame.PPM, getY() + 50 / OpenGunnerGame.PPM);
                } else {
                    bdef.position.set(getX() + 5 / OpenGunnerGame.PPM, getY() + 50 / OpenGunnerGame.PPM);
                }
                break;
            case GUN_UP_30:
                if (runningRight) {
                    bdef.position.set(getX() + 38 / OpenGunnerGame.PPM, getY() + 28 / OpenGunnerGame.PPM);
                } else {
                    bdef.position.set(getX() - 38 / OpenGunnerGame.PPM, getY() + 28 / OpenGunnerGame.PPM);
                }
                break;
            case GUN_UP_45:
                if (runningRight) {
                    bdef.position.set(getX() + 30 / OpenGunnerGame.PPM, getY() + 40 / OpenGunnerGame.PPM);
                } else {
                    bdef.position.set(getX() - 30 / OpenGunnerGame.PPM, getY() + 40 / OpenGunnerGame.PPM);
                }
                break;
            case GUN_DOWN_30:
                if (runningRight) {
                    bdef.position.set(getX() + 38 / OpenGunnerGame.PPM, getY() - 25 / OpenGunnerGame.PPM);
                } else {
                    bdef.position.set(getX() - 38 / OpenGunnerGame.PPM, getY() - 25 / OpenGunnerGame.PPM);
                }
                break;
            case GUN_DOWN_45:
                if (runningRight) {
                    bdef.position.set(getX() + 30 / OpenGunnerGame.PPM, getY() - 30 / OpenGunnerGame.PPM);
                } else {
                    bdef.position.set(getX() - 30 / OpenGunnerGame.PPM, getY() - 30 / OpenGunnerGame.PPM);
                }
                break;
            default:
                if (runningRight) {
                    bdef.position.set(getX() + 35 / OpenGunnerGame.PPM, getY() + 7 / OpenGunnerGame.PPM);
                } else {
                    bdef.position.set(getX() - 35 / OpenGunnerGame.PPM, getY() + 7 / OpenGunnerGame.PPM);
                }
                break;
        }

        bdef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bdef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(3 / OpenGunnerGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = OpenGunnerGame.PLAYER_SHOOT_BIT;
        fixtureDef.filter.maskBits = OpenGunnerGame.ENEMY_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }
}
