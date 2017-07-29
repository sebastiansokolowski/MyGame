package com.sebastian.sokolowski.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sebastian.sokolowski.game.scenes.Hud;
import com.sebastian.sokolowski.game.sprites.Bullet;
import com.sebastian.sokolowski.game.sprites.enemies.Enemy;
import com.sebastian.sokolowski.game.sprites.player.Player;
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

        if (fixtureB.getUserData() instanceof PlayerBullet && fixtureA.getUserData() instanceof Enemy) {
            Enemy enemy = (Enemy) fixtureA.getUserData();
            enemy.setDead();

            PlayerBullet playerBullet = (PlayerBullet) fixtureB.getUserData();
            playerBullet.setToDestroy();

            Hud.addScore(10);
            Gdx.app.log(TAG, "Contact PlayerBullet -> Enemy");
            return;
        }

        if (fixtureB.getUserData() instanceof Bullet && fixtureA.getUserData() instanceof Enemy) {
            Bullet bullet = (Bullet) fixtureB.getUserData();
            bullet.setToDestroy();
            Gdx.app.log(TAG, "Contact Bullet -> Enemy");
            return;
        }

        if (fixtureB.getUserData() instanceof Bullet && fixtureA.getUserData() instanceof Player) {
            Player player = (Player) fixtureA.getUserData();
            int life = Hud.removeLife(1);
            if (life <= 0) {
                player.setDead();
            }

            Bullet bullet = (Bullet) fixtureB.getUserData();
            bullet.setToDestroy();
            Gdx.app.log(TAG, "Contact Bullet -> Player");
            return;
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
