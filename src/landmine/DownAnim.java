package landmine;

import jig.Entity;
import jig.ResourceManager;

import landmine.LandMineGame;
import org.newdawn.slick.Animation;

/**
 * A class representing a transient explosion. The game should monitor
 * explosions to determine when they are no longer active and remove/hide
 * them at that point.
 */
class PDown extends Entity {
    private Animation pdown;

    public PDown(final float x, final float y) {
        super(x, y);
        pdown = new Animation(ResourceManager.getSpriteSheet(
                LandMineGame.PDOWN_RSC, 16, 16), 0, 0, 16, 0, true, 50,
                true);
        addAnimation(pdown);
        pdown.setLooping(false);
    }

    public boolean isActive() {
        return !pdown.isStopped();
    }
}
