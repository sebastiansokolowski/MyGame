package com.sebastian.sokolowski.game.sprites.objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Filter;
import com.sebastian.sokolowski.game.OpenGunnerGame;
import com.sebastian.sokolowski.game.screens.PlayScreen;
import com.sebastian.sokolowski.game.sprites.player.Player;

/**
 * Created by Sebastian Sokołowski on 10.08.17.
 */

public class Climber extends InteractiveObject {
    boolean currentCollision;

    public Climber(PlayScreen playScreen, MapObject object) {
        super(playScreen, object);

        currentCollision = false;
        fixture.setUserData(this);
    }

    @Override
    void setFixtureFilter() {
        Filter filter = new Filter();
        filter.categoryBits = OpenGunnerGame.CLIMBER_BIT;
        filter.maskBits = OpenGunnerGame.PLAYER_BIT;

        fixture.setFilterData(filter);
    }

    public void changedClimbCollision(Player player, boolean currentCollision) {
        if (this.currentCollision != currentCollision) {
            if (currentCollision) {
                player.setCurrentState(Player.State.CLIMB);
            } else {
                player.setCurrentState(Player.State.FALLING);
            }
            this.currentCollision = currentCollision;
        }
    }
}
