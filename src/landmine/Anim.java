package landmine;

import jig.ResourceManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Anim {


    public static SpriteSheet master = new SpriteSheet(ResourceManager.getImage(LandMineGame.MASTER_RSC), 16, 16);
    public static Animation pdown = new Animation(master, 1, 15, 1, 16, true, 50, true);;

    public static Animation getAnimation(Animation animation) throws SlickException {



        return pdown;

    }


}
