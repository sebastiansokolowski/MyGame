package com.sebastian.sokolowski.game.animations;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by Sebastian Sokołowski on 13.08.17.
 */

public class Waterfall {
    private TiledMap tiledMap;

    public Waterfall(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        initAnimation();
    }

    private void initAnimation() {
        Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>();

        Iterator<TiledMapTile> tiles = tiledMap.getTileSets().getTileSet("forest_0_4").iterator();
        while (tiles.hasNext()) {
            TiledMapTile tile = tiles.next();
            if (tile.getProperties().containsKey("animation") && tile.getProperties().get("animation", String.class).equals("waterfall")) {
                frameTiles.add((StaticTiledMapTile) tile);
            }
        }

        AnimatedTiledMapTile animatedTiledMapTile = new AnimatedTiledMapTile(0.1f, frameTiles);

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
        for (int x = 0; x != tiledMapTileLayer.getWidth(); x++) {
            for (int y = 0; y != tiledMapTileLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x, y);
                if (cell == null) {
                    continue;
                }
                if (cell.getTile().getProperties().containsKey("animation") && cell.getTile().getProperties().get("animation", String.class).equals("waterfall")) {
                    cell.setTile(animatedTiledMapTile);
                }
            }
        }
    }
}
