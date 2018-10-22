package landmine;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.*;

import static landmine.LandMineGame.PERSON_HIT_RSC;

public class Person extends Entity{

    public Level level;
    public boolean AI = false;
    private boolean invincible = false;
    private boolean damaged = false;
    private int invincible_time = 2000;
    private int deltaSoFar = 0;
    public int lives = 3;
    public int bombCount = 0;
    private float speed = 0.05f;
    public int bombTimer = 2400;
    public Vector bombPosition;
    public int pNumber;
    private boolean dead = false;
    private boolean isMoving = false;
    private Direction direction;
    private Vector movingTo;


    SpriteSheet master = new SpriteSheet(ResourceManager.getImage(LandMineGame.MASTER_RSC), 16, 16);
    private Animation death;

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

    public boolean isMoving() {
        return isMoving;
    }

    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction dir) {this.direction = dir; }

    public boolean isDead() { return dead; }
    public void setDead(Boolean dead) { this.dead = dead; }

    public Animation getDeath() { return death; }
    public void setDeath(Animation death) { this.death = death; }

    public Person(int x, int y, int pNumber) throws SlickException {
        super(x,y);

        this.pNumber = pNumber;
        if(pNumber != 0)
            AI = true;

        System.out.println("I'm Player number: " + pNumber);

        if(AI)
            System.out.println("I am player " + pNumber + " and I'm an AI.");
        else
            System.out.println("I am player " + pNumber + " and I'm a human.");

        level = Level.getInstance();

        if(pNumber == 1){
            SetColor(new Color(1.0f, 0.7f, 0.8f));

        } else if(pNumber == 2){
            SetColor(new Color(0.53f, 0.81f, 0.92f));

        } else if(pNumber == 3){
            SetColor(new Color(0.0f, 1.0f, 0.0f));

        }

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

        Animation death = new Animation(master, 14, 16, 14, 18, true, 100, true);
        death.addFrame(master.getSubImage(4, 13), 100);
        setDeath(death);

        direction = Direction.SOUTH;
        clearAnimations();

    }

    public void placeBomb(int x, int y) throws SlickException {
        bombPosition = new Vector(x*16+8, y*16+8);

        if(bombCount != 3 && !level.isBombHere(bombPosition)) {

            bombCount++;
            System.out.println("Player " + pNumber + " has " + bombCount + " bombs on the map.");
            level.bombList.add(new Bomb(x*16+8, y*16+8, bombTimer, pNumber));
            level.findSafeSpaces();
        }

    }

    public boolean canGo(Direction dir){
        int x = (int)getPosition().getX() / 16;
        int y = (int)getPosition().getY() / 16;

        switch (dir){
            case NORTH:
                if(level.notAWall(x,y-1))
                    return true;
                break;
            case SOUTH:
                if(level.notAWall(x, y+1))
                    return true;
                break;
            case EAST:
                if(level.notAWall(x+1, y))
                    return true;
                break;
            case WEST:
                if(level.notAWall(x-1, y))
                    return true;
                break;

        }

        return false;

    }

    public void movement(Direction dir){
        if(isMoving) {
            return;
        }

        clearAnimations();

        if(!canGo(dir)) {
            direction = dir;
            addAnimation(direction.getIdle());
            if(damaged)
                addAnimation(getDeath());

            return;

        } else {

            direction = dir;
            addAnimation(direction.getWalk());
            if(damaged)
                addAnimation(getDeath());

            switch (direction) {
                case NORTH:
                    movingTo = getPosition().add(new Vector(0, -16.0f));
                    break;
                case SOUTH:
                    movingTo = getPosition().add(new Vector(0, 16.0f));
                    break;
                case EAST:
                    movingTo = getPosition().add(new Vector(16.0f, 0f));
                    break;
                case WEST:
                    movingTo = getPosition().add(new Vector(-16.0f, 0));
                    break;
            }
        }

        isMoving = true;

    }

    public void update(int delta){
        if(!dead) {
            deltaSoFar += delta;

            if (isMoving) {
                double angle = getPosition().angleTo(movingTo);
                setPosition(getPosition().add(Vector.getUnit(angle).scale(speed * delta)));
                if (getPosition().epsilonEquals(movingTo, speed * delta)) {
                    isMoving = false;

                    setPosition(movingTo);
                    clearAnimations();
                    addAnimation(direction.getIdle());
                    if(damaged)
                        addAnimation(getDeath());
                    //System.out.printf("Player " + pNumber + " moved to: %s\n", getPosition());
                }

            }

            if (deltaSoFar >= invincible_time && invincible) {
                removeAnimation(getDeath());
                System.out.println("Player " + pNumber + " is no longer invincible...");
                invincible = false;
                damaged = false;
            }
        }



    }

    public void takeLife(){

        if(!invincible) {

            lives--;
            damaged = true;

            System.out.println("Player " + pNumber + " has " + lives + " lives remaining.");
            if(pNumber == 0){
                PlayingState.score = PlayingState.score - 200;
            }

            if (lives <= 0) {
                //end character
                System.out.println("Player " + pNumber + " is dead.");
                dead = true;
            } else {
                addAnimation(getDeath());
                ResourceManager.getSound(PERSON_HIT_RSC).play();
            }

        }

        //Set invincible here with a small timer in the update loop.
        invincible = true;
        deltaSoFar = 0;
        if(!dead)
            System.out.println("Player number " + pNumber + " is invincible.");

    }

    private void clearAnimations(){
        removeAnimation(Direction.SOUTH.getWalk());
        removeAnimation(Direction.NORTH.getWalk());
        removeAnimation(Direction.WEST.getWalk());
        removeAnimation(Direction.EAST.getWalk());

        removeAnimation(Direction.SOUTH.getIdle());
        removeAnimation(Direction.NORTH.getIdle());
        removeAnimation(Direction.WEST.getIdle());
        removeAnimation(Direction.EAST.getIdle());

        removeAnimation(getDeath());

    }

    public void initialAnimation(){
        addAnimation(direction.getIdle());
    }

    @Override
    public void render(Graphics g){
        super.render(g);
        if(dead)
            return;

        ArrayList<Bomb> shouldDelete = new ArrayList<>();

        for(Bomb bomb: level.bombList){
            if(bomb.needsDeletion){
                shouldDelete.add(bomb);
            }else {
                bomb.render(g);
            }
        }

        for(Bomb bomb : shouldDelete){
            if(bomb.pNumber == pNumber) {
                bombCount--;
                System.out.println("Player " + pNumber + " has " + bombCount + " many bombs on the map.");
                level.bombList.remove(bomb);
                level.findSafeSpaces();
            }
        }
    }

}
