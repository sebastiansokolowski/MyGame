package com.sebastian.sokolowski.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sebastian.sokolowski.game.scenes.Hud;
import com.sebastian.sokolowski.game.sprites.enemies.Enemy;
import com.sebastian.sokolowski.game.sprites.player.PlayerBullet;

/**
 * Created by Sebastian Sokołowski on 05.07.17.
 */

public class WorldContactListener implements ContactListener {
    public final static String TAG = WorldContactListener.class.getSimpleName();

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null ||
                fixtureA.getUserData() == null || fixtureB.getUserData() == null) {
            return;
        }

        Gdx.app.log(TAG, "contact!!");

        if (fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() instanceof Enemy) {
            Enemy enemy = (Enemy) fixtureB.getUserData();
            PlayerBullet playerBullet = (PlayerBullet) fixtureA.getUserData();

            playerBullet.setToDestroy();
            enemy.setDead();
            Hud.addScore(10);

            Gdx.app.log(TAG, "hit!!");
        } else if (fixtureB.getUserData() instanceof PlayerBullet && fixtureA.getUserData() instanceof Enemy) {
            Enemy enemy = (Enemy) fixtureA.getUserData();
            PlayerBullet playerBullet = (PlayerBullet) fixtureB.getUserData();
            playerBullet.setToDestroy();
            enemy.setDead();

            Hud.addScore(10);
            Gdx.app.log(TAG, "hit!!");
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
