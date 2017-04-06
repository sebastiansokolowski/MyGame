package com.sebastian.sokolowski.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Sebastian Sokołowski on 07.04.17.
 */

public class Ladder extends InteractiveTileObject {
    public Ladder(World world, TiledMap tiledMap, Rectangle rectangle) {
        super(world, tiledMap, rectangle);
    }
}
