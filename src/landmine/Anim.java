package landmine;

import jig.ResourceManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Anim {


    public static SpriteSheet master = new SpriteSheet(ResourceManager.getImage(LandMineGame.MASTER_RSC), 16, 16);

    public enum Direction{
        EAST,
        WEST,
        SOUTH,
        NORTH
    }

    public Anim(){

    }



    public static Animation getAnimation(Direction dir) throws SlickException {

        Animation down = new Animation(master, 2, 16, 3, 16, true, 250, true);

        Animation up = new Animation(master, 8, 16, 9, 16, true, 250, true);

        Animation right = new Animation(master, 5, 16, 7, 16, true, 250, true);
        right.addFrame(right.getImage(1),250);

        Image[] left_a = new Image[4];
        left_a[0] = master.getSubImage(5,16).getFlippedCopy(true, false);
        left_a[1] = master.getSubImage(6, 16).getFlippedCopy(true, false);
        left_a[2] = master.getSubImage(7, 16).getFlippedCopy(true, false);
        left_a[3] = master.getSubImage(6, 16).getFlippedCopy(true, false);
        Animation left = new Animation(left_a, 250);

        return down;

    }


}
