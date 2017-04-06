package com.sebastian.sokolowski.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sebastian.sokolowski.game.MyGdxGame;

/**
 * Created by Sebastian Sokołowski on 07.04.17.
 */

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap tiledMap;
    protected TiledMapTile tiledMapTile;
    protected Rectangle rectangle;
    protected Body body;

    public InteractiveTileObject(World world, TiledMap tiledMap, Rectangle rectangle) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.rectangle = rectangle;


        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / MyGdxGame.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / MyGdxGame.PPM);

        body = world.createBody(bodyDef);

        polygonShape.setAsBox((rectangle.getWidth() / 2) / MyGdxGame.PPM, (rectangle.getHeight() / 2) / MyGdxGame.PPM);
        fixtureDef.shape = polygonShape;

        body.createFixture(fixtureDef);
    }
}
