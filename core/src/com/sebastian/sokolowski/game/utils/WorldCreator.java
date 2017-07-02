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
import com.sebastian.sokolowski.game.screens.PlayScreen;
import com.sebastian.sokolowski.game.sprites.enemies.basic.BasicEnemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian Sokołowski on 07.04.17.
 */

public class WorldCreator {

    private List<BasicEnemy> basicEnemies = new ArrayList<BasicEnemy>();

    public WorldCreator(TiledMap tiledMap, World world, PlayScreen playScreen) {
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

        // basic enemy
        for (MapObject mapObject : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            basicEnemies.add(new BasicEnemy(world, rectangle.getX() / MyGdxGame.PPM, rectangle.getY() / MyGdxGame.PPM, playScreen));
        }
    }


    public List<BasicEnemy> getEnemy() {
        return basicEnemies;
    }
}
