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
    private Vector2 bulletVector = new Vector2(10, 10);
    private Body body;
    private boolean destroyed;
    float stateTime;
    float angle;
    private boolean setToDestroy;

    public Bullet(PlayScreen playScreen, float x, float y, Vector2 vector2) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        angle = vector2.angle();

        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Tiles/Player/bullet.png")), 36, 18);
        setRegion(textureRegion);
        bulletVector.setAngle(angle);
        setBounds(x, y, 36 / MyGdxGame.PPM, 18 / MyGdxGame.PPM);
        setOriginCenter();
        setRotation(angle);
        defineBullet();
    }

    public void defineBullet() {

        BodyDef bdef = new BodyDef();

        if (angle >= 0 && angle <= 90 ||
                angle >= 270 && angle <= 360) {
            // right
            bdef.position.set(getX() + 25 / MyGdxGame.PPM, getY() + 5 / MyGdxGame.PPM);
        } else if (angle >= 75 && angle <= 105) {
            // up
            bdef.position.set(getX(), getY() + 25 / MyGdxGame.PPM);
        } else if (angle >= 255 && angle <= 285) {
            // down
            bdef.position.set(getX(), getY() - 25 / MyGdxGame.PPM);
        } else {
            // left
            bdef.position.set(getX() - 25 / MyGdxGame.PPM, getY() + 5 / MyGdxGame.PPM);
        }

        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(3 / MyGdxGame.PPM);

        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.002f;

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

        System.out.println(getY() * MyGdxGame.PPM);

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
