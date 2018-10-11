package landmine;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.*;

public class Person extends Entity{

    private float speed = 0.05f;
    public int bombTimer = 3000;
    public ArrayList<Bomb> bombList = new ArrayList<>(3);

    SpriteSheet master = new SpriteSheet(ResourceManager.getImage(LandMineGame.MASTER_RSC), 16, 16);

    public Level wall;

    public enum Direction{
        NORTH,
        SOUTH,
        EAST,
        WEST;

        private Animation idle;
        private Animation walk;

        public void setIdle(Animation idle) {
            this.idle = idle;
        }
        public void setWalk(Animation walk) {
            this.walk = walk;
        }
        public Animation getIdle() {
            return idle;
        }
        public Animation getWalk() {
            return walk;
        }

    }
    private boolean isMoving = false;
    private Direction direction;
    private Vector movingTo;


    public boolean isMoving() {
        return isMoving;
    }

    public Direction getDirection() {
        return direction;
    }

    public Person(int x, int y, Level wall) throws SlickException {
        super(x,y);

        this.wall = wall;

        Direction.SOUTH.setWalk(new Animation(master, 2, 16, 3, 16, true, 250, true));

        Direction.NORTH.setWalk(new Animation(master, 8, 16, 9, 16, true, 250, true));

        Animation right = new Animation(master, 5, 16, 7, 16, true, 250, true);
        right.addFrame(right.getImage(1),250);
        Direction.EAST.setWalk(right);

        Image[] left_a = new Image[4];
        left_a[0] = master.getSubImage(5,16).getFlippedCopy(true, false);
        left_a[1] = master.getSubImage(6, 16).getFlippedCopy(true, false);
        left_a[2] = master.getSubImage(7, 16).getFlippedCopy(true, false);
        left_a[3] = master.getSubImage(6, 16).getFlippedCopy(true, false);
        Animation left = new Animation(left_a, 250);
        Direction.WEST.setWalk(left);

        Direction.NORTH.setIdle(new Animation(master, 0, 16, 0, 16, false, 250, true));
        Direction.EAST.setIdle(new Animation(master, 4, 16, 4, 16, false, 250, true));
        Direction.SOUTH.setIdle(new Animation(master, 1, 16, 1, 16, false, 250, true));
        Image[] left_i = new Image[1];
        left_i[0] = master.getSubImage(4, 16).getFlippedCopy(true, false);
        Direction.WEST.setIdle(new Animation(left_i, 250));

        direction = Direction.SOUTH;
        addAnimation(direction.getIdle());
    }

    public void placeBomb(int x, int y) throws SlickException {

        System.out.println("I want to place a bomb at x: " + x + " y: " + y);
        System.out.println("Adding a bomb to the bombList");
        if(bombList.size() < 3) {
            bombList.add(new Bomb(x*16+8, y*16+8, bombTimer));
        }

    }

    public boolean canGo(Direction dir){
        int x = (int)getPosition().getX() / 16;
        int y = (int)getPosition().getY() / 16;

        switch (dir){
            case NORTH:
                if(wall.notAWall(x,y-1))
                    return true;
                break;
            case SOUTH:
                if(wall.notAWall(x, y+1))
                    return true;
                break;
            case EAST:
                if(wall.notAWall(x+1, y))
                    return true;
                break;
            case WEST:
                if(wall.notAWall(x-1, y))
                    return true;
                break;

        }

        return false;

    }

    public void movement(Direction dir){
        if(isMoving)
            return;

        if(!canGo(dir))
            return;

        removeAnimation(direction.getIdle());
        removeAnimation(direction.getWalk());
        direction = dir;
        addAnimation(direction.getWalk());

        switch (direction){
            case NORTH:
                movingTo = getPosition().add(new Vector(0,-16.0f));
                break;
            case SOUTH:
                movingTo = getPosition().add(new Vector(0,16.0f));
                break;
            case EAST:
                movingTo = getPosition().add(new Vector(16.0f, 0f));
                break;
            case WEST:
                movingTo = getPosition().add(new Vector(-16.0f, 0));
                break;
        }

        isMoving = true;

    }

    public void update(int delta){
        if(isMoving){
            double angle = getPosition().angleTo(movingTo);
            setPosition(getPosition().add(Vector.getUnit(angle).scale(speed*delta)));
            if(getPosition().epsilonEquals(movingTo, speed*delta)){
                isMoving = false;

                setPosition(movingTo);
                removeAnimation(direction.getWalk());
                addAnimation(direction.getIdle());
                System.out.printf("Moved to: %s\n", getPosition());
            }

        }

        for(Bomb bomb: bombList){
            bomb.update(delta);
        }

    }

    @Override
    public void render(Graphics g){
        super.render(g);

        ArrayList<Bomb> shouldDelete = new ArrayList<>();

        for(Bomb bomb: bombList){
            if(bomb.needsDeletion){
                shouldDelete.add(bomb);
            }else {
                bomb.render(g);
            }
        }

        for(Bomb bomb : shouldDelete){
            bombList.remove(bomb);
        }
    }

}