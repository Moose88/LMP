package landmine;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.*;

import landmine.Anim;

import org.newdawn.slick.SlickException;

import static landmine.Anim.pdown;

public class Player1 extends Entity {

//    private Direction direction;

    public boolean puttingBomb = false;

    SpriteSheet master = new SpriteSheet(ResourceManager.getImage(LandMineGame.MASTER_RSC), 16, 16);


    public Player1(int x, int y) throws SlickException {
        super(x,y);

        addImageWithBoundingBox(master.getSubImage(1,15));


    }


}
