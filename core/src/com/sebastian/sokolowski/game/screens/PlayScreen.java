package com.sebastian.sokolowski.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sebastian.sokolowski.game.MyGdxGame;
import com.sebastian.sokolowski.game.scenes.Hud;

/**
 * Created by Sebastian Sokołowski on 05.04.17.
 */

public class PlayScreen implements Screen {
    private final MyGdxGame myGdxGame;
    private OrthographicCamera orthographicCamera;
    private Viewport viewPort;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthoCachedTiledMapRenderer orthoCachedTiledMapRenderer;
    Hud hud;

    public PlayScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        orthographicCamera = new OrthographicCamera();
        viewPort = new FitViewport(MyGdxGame.V_WIDTH, myGdxGame.V_HEIGHT, orthographicCamera);

        hud = new Hud(myGdxGame.batch);

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("level1.tmx");
        orthoCachedTiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
        orthographicCamera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);
    }

    @Override
    public void show() {

    }

    public void update(float delta) {
        handleInput(delta);

        orthographicCamera.update();
        orthoCachedTiledMapRenderer.setView(orthographicCamera);
    }

    private void handleInput(float delta) {
        if (Gdx.input.isTouched()) {
            orthographicCamera.position.x += 400 * delta;
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthoCachedTiledMapRenderer.render();

        myGdxGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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

    }
}
