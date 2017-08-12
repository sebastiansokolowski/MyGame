package com.sebastian.sokolowski.game.sprites.objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sebastian.sokolowski.game.OpenGunnerGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;

/**
 * Created by Sebastian Sokołowski on 07.04.17.
 */

public abstract class InteractiveObject {
    final Fixture fixture;
    final Body body;

    public InteractiveObject(PlayScreen playScreen, MapObject object) {
        World world = playScreen.getWorld();
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / OpenGunnerGame.PPM, rectangle.getY() / OpenGunnerGame.PPM);

        body = world.createBody(bodyDef);

        polygonShape.setAsBox(rectangle.getWidth() / 2 / OpenGunnerGame.PPM, rectangle.getHeight() / 2 / OpenGunnerGame.PPM);
        fixtureDef.shape = polygonShape;

        fixture = body.createFixture(fixtureDef);

        setFixtureFilter();
    }

    abstract void setFixtureFilter();
}
