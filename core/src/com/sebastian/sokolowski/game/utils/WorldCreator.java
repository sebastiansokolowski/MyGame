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
import com.sebastian.sokolowski.game.OpenGunnerGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;
import com.sebastian.sokolowski.game.sprites.enemies.Enemy;
import com.sebastian.sokolowski.game.sprites.enemies.BasicEnemy;
import com.sebastian.sokolowski.game.sprites.enemies.Turret;
import com.sebastian.sokolowski.game.sprites.enemies.Turret2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian Sokołowski on 07.04.17.
 */

public class WorldCreator {

    private List<Enemy> basicEnemies = new ArrayList<Enemy>();

    public WorldCreator(TiledMap tiledMap, World world, PlayScreen playScreen) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //ground
        for (MapObject mapObject : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / OpenGunnerGame.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / OpenGunnerGame.PPM);

            body = world.createBody(bodyDef);

            polygonShape.setAsBox((rectangle.getWidth() / 2) / OpenGunnerGame.PPM, (rectangle.getHeight() / 2) / OpenGunnerGame.PPM);
            fixtureDef.shape = polygonShape;

            body.createFixture(fixtureDef);
        }

        // basic enemy
        for (MapObject mapObject : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            basicEnemies.add(new BasicEnemy(playScreen, rectangle.getX() / OpenGunnerGame.PPM, rectangle.getY() / OpenGunnerGame.PPM));
        }

        // turret enemy
        for (MapObject mapObject : tiledMap.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            basicEnemies.add(new Turret(playScreen, rectangle.getX() / OpenGunnerGame.PPM, rectangle.getY() / OpenGunnerGame.PPM));
        }

        // turret2 enemy
        for (MapObject mapObject : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            basicEnemies.add(new Turret2(playScreen, rectangle.getX() / OpenGunnerGame.PPM, rectangle.getY() / OpenGunnerGame.PPM));
        }
    }


    public List<Enemy> getEnemies() {
        return basicEnemies;
    }
}
