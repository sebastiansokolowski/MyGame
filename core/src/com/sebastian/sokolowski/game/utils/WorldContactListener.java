package com.sebastian.sokolowski.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sebastian.sokolowski.game.OpenGunnerGame;
import com.sebastian.sokolowski.game.scenes.Hud;
import com.sebastian.sokolowski.game.sprites.Bullet;
import com.sebastian.sokolowski.game.sprites.enemies.Enemy;
import com.sebastian.sokolowski.game.sprites.objects.Climber;
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

        int collision = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (collision) {
            case OpenGunnerGame.PLAYER_SHOOT_BIT | OpenGunnerGame.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == OpenGunnerGame.PLAYER_SHOOT_BIT) {
                    collision((Enemy) fixtureB.getUserData(), (PlayerBullet) fixtureA.getUserData());
                } else {
                    collision((Enemy) fixtureA.getUserData(), (PlayerBullet) fixtureB.getUserData());
                }
                break;
            case OpenGunnerGame.ENEMY_SHOOT_BIT | OpenGunnerGame.PLAYER_BIT:
                if (fixtureA.getFilterData().categoryBits == OpenGunnerGame.ENEMY_SHOOT_BIT) {
                    collision((Player) fixtureB.getUserData(), (Bullet) fixtureA.getUserData());
                } else {
                    collision((Player) fixtureA.getUserData(), (Bullet) fixtureB.getUserData());
                }
                break;
            case OpenGunnerGame.CLIMBER_BIT | OpenGunnerGame.PLAYER_BIT:
                if (fixtureA.getFilterData().categoryBits == OpenGunnerGame.CLIMBER_BIT) {
                    collision((Player) fixtureB.getUserData(), (Climber) fixtureA.getUserData(), true);
                } else {
                    collision((Player) fixtureA.getUserData(), (Climber) fixtureB.getUserData(), true);
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null ||
                fixtureA.getUserData() == null || fixtureB.getUserData() == null) {
            return;
        }

        int collision = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (collision) {
            case OpenGunnerGame.CLIMBER_BIT | OpenGunnerGame.PLAYER_BIT:
                if (fixtureA.getFilterData().categoryBits == OpenGunnerGame.CLIMBER_BIT) {
                    collision((Player) fixtureB.getUserData(), (Climber) fixtureA.getUserData(), false);
                } else {
                    collision((Player) fixtureA.getUserData(), (Climber) fixtureB.getUserData(), false);
                }
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //nothing to do
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //nothing to do
    }

    private void collision(Player player, Climber climber, boolean currentCollision) {
        climber.changedClimbCollision(player, currentCollision);

        Gdx.app.log(TAG, "Contact Player -> Climber");
    }

    private void collision(Player player, Bullet bullet) {
        int life = Hud.removeLife(1);
        if (life <= 0) {
//            player.setDead();
        }

        bullet.setToDestroy();
        Gdx.app.log(TAG, "Contact Bullet -> Player");
    }

    private void collision(Enemy enemy, PlayerBullet playerBullet) {
        enemy.setDead();

        playerBullet.setToDestroy();

        Hud.addScore(10);
        Gdx.app.log(TAG, "Contact PlayerBullet -> Enemy");
    }
}
