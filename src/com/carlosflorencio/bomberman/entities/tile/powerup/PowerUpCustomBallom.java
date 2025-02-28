package com.carlosflorencio.bomberman.entities.tile.powerup;

import com.carlosflorencio.bomberman.Game;
import com.carlosflorencio.bomberman.entities.Entity;
import com.carlosflorencio.bomberman.entities.mob.Player;
import com.carlosflorencio.bomberman.graphics.Sprite;

public class PowerUpCustomBallom extends Powerup {

    public PowerUpCustomBallom(int x, int y, int level, Sprite sprite) {
        super(x, y, level, sprite);
        setValues();
    }

    @Override
    public boolean collide(Entity e) {

        if (e instanceof Player) {
            ((Player) e).addPowerup(this);
            remove();
            return true;
        }

        return false;
    }

    @Override
    public void setValues() {
        _active = true;
        Game.hesoyam();
    }

}
