package com.sebastian.sokolowski.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
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
    private Hud hud;

    //tiled map
    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthoCachedTiledMapRenderer orthoCachedTiledMapRenderer;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    public PlayScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        orthographicCamera = new OrthographicCamera();
        viewPort = new FitViewport(MyGdxGame.V_WIDTH, myGdxGame.V_HEIGHT, orthographicCamera);

        hud = new Hud(myGdxGame.batch);

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("level1.tmx");
        orthoCachedTiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
        orthographicCamera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //ground
        for (MapObject mapObject : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);

            body = world.createBody(bodyDef);

            polygonShape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            fixtureDef.shape = polygonShape;

            body.createFixture(fixtureDef);
        }

        //beers
        for (MapObject mapObject : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);

            body = world.createBody(bodyDef);

            polygonShape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            fixtureDef.shape = polygonShape;

            body.createFixture(fixtureDef);
        }

        //ladders
        for (MapObject mapObject : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);

            body = world.createBody(bodyDef);

            polygonShape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            fixtureDef.shape = polygonShape;

            body.createFixture(fixtureDef);
        }
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
        Gdx.gl.glEnable(GL20.GL_BLEND);


        // render map
        orthoCachedTiledMapRenderer.render();

        box2DDebugRenderer.render(world, orthographicCamera.combined);

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
