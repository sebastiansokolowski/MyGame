package com.sebastian.sokolowski.game.sprites.enemies.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;
import com.sebastian.sokolowski.game.sprites.enemies.Bullet;

/**
 * Created by Sebastian Sokołowski on 02.07.17.
 */

public class BasicEnemyBullet extends Bullet {
    public BasicEnemyBullet(PlayScreen playScreen, float x, float y, boolean runningRight) {
        super(new TextureRegion(new Texture(Gdx.files.internal("Tiles/Player/bullet.png")), 36, 18),
                playScreen, x, y, runningRight ? 0 : 180);
    }

    @Override
    public void defineBody() {
        BodyDef bdef = new BodyDef();

        if (angle >= 0 && angle <= 90) {
            bdef.position.set(getX() + 35 / MyGdxGame.PPM, getY() + 7 / MyGdxGame.PPM);
        } else {
            bdef.position.set(getX() - 35 / MyGdxGame.PPM, getY() + 7 / MyGdxGame.PPM);
        }

        bdef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bdef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(3 / MyGdxGame.PPM);

        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef);
    }

}
