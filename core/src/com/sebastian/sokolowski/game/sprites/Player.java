package com.sebastian.sokolowski.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;

/**
 * Created by Sebastian Sokołowski on 06.04.17.
 */

public class Player extends Sprite {
    public World world;
    public Body body;
    private TextureRegion textureRegion;

    public Player(World world, PlayScreen playScreen) {
        super(playScreen.getTextureAtlas().findRegion("basic"));
        this.world = world;
        definePlayer();
        textureRegion = new TextureRegion(getTexture(), 0, 0, 70, 70);
        setBounds(0, 0, 70 / MyGdxGame.PPM, 70 / MyGdxGame.PPM);
        setRegion(textureRegion);
    }

    private void definePlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((MyGdxGame.V_WIDTH / 2) / MyGdxGame.PPM, 210 / MyGdxGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(20 / MyGdxGame.PPM);

        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef);
    }

    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }
}
