package com.sebastian.sokolowski.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sebastian.sokolowski.game.Controller;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.scenes.Hud;
import com.sebastian.sokolowski.game.sprites.enemies.Enemy;
import com.sebastian.sokolowski.game.sprites.player.Player;
import com.sebastian.sokolowski.game.utils.WorldCreator;

/**
 * Created by Sebastian Sokołowski on 05.04.17.
 */

public class PlayScreen implements Screen {
    private final MyGdxGame game;
    private final Controller controller;
    private final WorldCreator worldCreator;

    private OrthographicCamera orthographicCamera;
    private Viewport viewPort;
    private Hud hud;

    //tiled map
    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Player player;

    public PlayScreen(MyGdxGame game) {
        this.game = game;


        orthographicCamera = new OrthographicCamera();
        viewPort = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, game.V_HEIGHT / MyGdxGame.PPM, orthographicCamera);

        hud = new Hud(game.batch);

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("level1.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / MyGdxGame.PPM);
        orthographicCamera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);


        world = new World(new Vector2(0, -10), true);

        box2DDebugRenderer = new Box2DDebugRenderer();

        player = new Player(world, this);

        controller = new Controller(player);

        worldCreator = new WorldCreator(tiledMap, world, this);
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void show() {

    }

    public void update(float delta) {
        controller.update();

        world.step(1 / 60f, 6, 2);

        if (player.currentState != Player.State.DEAD) {
            orthographicCamera.position.x = player.body.getPosition().x;
        }

        for (Enemy enemy : worldCreator.getEnemies()) {
            enemy.update(delta);
        }
        player.update(delta);

        orthographicCamera.update();
        orthogonalTiledMapRenderer.setView(orthographicCamera);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render map
        orthogonalTiledMapRenderer.render();

        box2DDebugRenderer.render(world, orthographicCamera.combined);

        game.batch.setProjectionMatrix(orthographicCamera.combined);
        game.batch.begin();
        for (Enemy enemy: worldCreator.getEnemies()) {
            enemy.draw(game.batch);
        }
        player.draw(game.batch);
        game.batch.end();

        //draw
        controller.draw();
        hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean gameOver() {
        if (player.currentState == Player.State.DEAD) {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        controller.resize(width, height);
        viewPort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
        hud.dispose();
    }
}
