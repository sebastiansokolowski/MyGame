package com.sebastian.sokolowski.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.sprites.Beer;
import com.sebastian.sokolowski.game.sprites.Ladder;

/**
 * Created by Sebastian Sokołowski on 07.04.17.
 */

public class WorldCreator {

    public WorldCreator(TiledMap tiledMap, World world) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //ground
        for (MapObject mapObject : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / MyGdxGame.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / MyGdxGame.PPM);

            body = world.createBody(bodyDef);

            polygonShape.setAsBox((rectangle.getWidth() / 2) / MyGdxGame.PPM, (rectangle.getHeight() / 2) / MyGdxGame.PPM);
            fixtureDef.shape = polygonShape;

            body.createFixture(fixtureDef);
        }


        //beers
        for (MapObject mapObject : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            new Beer(world, tiledMap, rectangle);
        }

        //ladders
        for (MapObject mapObject : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            new Ladder(world, tiledMap, rectangle);
        }
    }
}
