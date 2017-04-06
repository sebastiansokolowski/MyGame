package com.sebastian.sokolowski.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sebastian.sokolowski.game.MyGdxGame;

/**
 * Created by Sebastian Sokołowski on 06.04.17.
 */

public class Player extends Sprite {
    public World world;
    public Body body;

    public Player(World world) {
        this.world = world;

        definePlayer();
    }

    private void definePlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(210 / MyGdxGame.PPM, 210 / MyGdxGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(50 / MyGdxGame.PPM);

        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef);
    }

    public void update(float delta) {

    }
}
