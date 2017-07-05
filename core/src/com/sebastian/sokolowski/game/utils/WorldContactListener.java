package com.sebastian.sokolowski.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sebastian.sokolowski.game.sprites.enemies.Enemy;
import com.sebastian.sokolowski.game.sprites.player.Bullet;

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

        if (fixtureA.getUserData() instanceof Bullet && fixtureB.getUserData() instanceof Enemy) {
            Enemy enemy = (Enemy) fixtureB.getUserData();
            Bullet bullet = (Bullet) fixtureA.getUserData();

            bullet.setToDestroy();
            enemy.setDead();

            Gdx.app.log(TAG, "hit!!");
        } else if (fixtureB.getUserData() instanceof Bullet && fixtureA.getUserData() instanceof Enemy) {
            Enemy enemy = (Enemy) fixtureA.getUserData();
            Bullet bullet = (Bullet) fixtureB.getUserData();
            bullet.setToDestroy();
            enemy.setDead();
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
