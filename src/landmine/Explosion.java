package landmine;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;


public class Explosion extends Entity {

    SpriteSheet master = new SpriteSheet(ResourceManager.getImage(LandMineGame.MASTER_RSC), 16, 16);
    public enum Direction{NORTH,SOUTH,EAST,WEST}
    Level level = Level.getInstance();
    public int gridX;
    public int gridY;
    public int scaleX;
    public int scaleY;

    private Explosion next;

    public Explosion(int x, int y, Direction direction){
        super(x,y);

        System.out.println("Exploding!");
        gridX = x/16;
        gridY = y/16;

        switch (direction){
            case NORTH:
                if(level.notAWall(gridX, gridY-1)){
                    System.out.println("Explosion x: " + x + " y: " + y );
                    next = new Explosion(x,y-16,Direction.NORTH);
                    addImageWithBoundingBox(master.getSubImage(14, 14));
                } else {
                    addImageWithBoundingBox(master.getSubImage(14, 13));
                }
                break;
            case SOUTH:
                if(level.notAWall(gridX, gridY+1)){
                    System.out.println("Explosion x: " + x + " y: " + y );
                    next = new Explosion(x, y+16, Direction.SOUTH);
                    addImageWithBoundingBox(master.getSubImage(14,14));
                } else {
                    addImageWithBoundingBox(master.getSubImage(14, 15));
                }
                break;
            case EAST:
                if(level.notAWall(gridX+1, gridY)){
                    System.out.println("Explosion x: " + x + " y: " + y );
                    next = new Explosion(x+16, y, Direction.EAST);
                    addImageWithBoundingBox(master.getSubImage(1, 18));
                } else {
                    addImageWithBoundingBox(master.getSubImage(3, 18));
                }
                break;
            case WEST:
                if(level.notAWall(gridX-1, gridY)){
                    System.out.println("Explosion x: " + x + " y: " + y );
                    next = new Explosion(x-16, y, Direction.WEST);
                    addImageWithBoundingBox(master.getSubImage(1, 18));
                } else {
                    addImageWithBoundingBox(master.getSubImage(0,18));
                }
                break;
        }


    }

    @Override
    public void render(Graphics g){
        super.render(g);
        if(next != null) {
            next.render(g);
        }
    }

}
