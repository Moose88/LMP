package landmine;

import jig.Vector;
import org.newdawn.slick.SlickException;

import java.util.Iterator;
import java.util.LinkedList;

public class Dinky extends Person {
    private boolean isSissyBitch;
    private boolean standby;
    private int deltaSoFar;
    private int timer = 2000;
    LinkedList<Vector> attack = new LinkedList<>();

    public Dinky(int x, int y, int pNumber) throws SlickException {
        super(x, y, pNumber);
        System.out.println("I is Dinky");
        isSissyBitch = true;
        standby = true;
        deltaSoFar = 0;

    }

    public void update(int delta) {
        super.update(delta);

        // For some reason, this is still running on an array out of bounds here.
        if(level.playerList.get(pNumber) != null) {
            if (standby) {
                deltaSoFar += delta;
                if (deltaSoFar >= timer)
                    standby = false;

            } else {

                if (isSissyBitch) {
                    // System.out.println("Dinky " + this.pNumber + " is a Sissy Bitch");
                    // If trying to run away
                    int x = (int) getPosition().getX() / 16;
                    int y = (int) getPosition().getY() / 16;
                    Vector me = new Vector(x, y);
                    Vector pi = dykeStraw.getSafeInstance().getPi(me);
                    if (pi != null) {
                        if (pi.getY() > y)
                            level.playerList.get(pNumber).movement(Direction.SOUTH);
                        else if (pi.getY() < y)
                            level.playerList.get(pNumber).movement(Direction.NORTH);
                        else if (pi.getX() > x)
                            level.playerList.get(pNumber).movement(Direction.EAST);
                        else if (pi.getX() < x)
                            level.playerList.get(pNumber).movement(Direction.WEST);
                    } else {
                        isSissyBitch = false;
                    }

                } else {

                    attack = new LinkedList<>();
                    int[][] battlefield = level.safespace;

                    // Set ENEMY player positions here
                    // We want to find the safe space, that is between the lines of an enemy placement
                    Iterator<Person> it = level.playerList.iterator();
                    while (it.hasNext()) {
                        Person person = it.next();
                        if (person.pNumber != this.pNumber) {
                            int ex = (int) person.getX() / 16;
                            int ey = (int) person.getY() / 16;

                            //From enemy position, find all safe spaces to the north, east, south, and west.
                            while (level.notAWall(ex, ey)) {
                                if (battlefield[ex][ey] == 1)
                                    attack.add(new Vector(ex, ey));

                                ex++;
                            }

                            ex = (int) person.getX() / 16;
                            ey = (int) person.getY() / 16;

                            while (level.notAWall(ex, ey)) {
                                if (battlefield[ex][ey] == 1)
                                    attack.add(new Vector(ex, ey));

                                ex--;
                            }

                            ex = (int) person.getX() / 16;
                            ey = (int) person.getY() / 16;

                            while (level.notAWall(ex, ey)) {
                                if (battlefield[ex][ey] == 1)
                                    attack.add(new Vector(ex, ey));

                                ey++;
                            }

                            ex = (int) person.getX() / 16;
                            ey = (int) person.getY() / 16;

                            while (level.notAWall(ex, ey)) {
                                if (battlefield[ex][ey] == 1)
                                    attack.add(new Vector(ex, ey));

                                ey--;
                            }
                        }
                    }

                    //System.out.println("Source Vector Array is " + attack.size() + " long");

                    Vector[] s = attack.toArray(new Vector[attack.size()]);
                    dykeStraw.getAttackInstance().perform(battlefield, s);

                    // attack
                    int x = (int) getPosition().getX() / 16;
                    int y = (int) getPosition().getY() / 16;

                    //This needs to be sources
                    Vector me = new Vector(x, y);

                    //This is where I want to go!
                    Vector pi = dykeStraw.getAttackInstance().getPi(me);


                    if (pi != null) {

                        if (pi.getY() > y)
                            level.playerList.get(pNumber).movement(Direction.SOUTH);
                        else if (pi.getY() < y)
                            level.playerList.get(pNumber).movement(Direction.NORTH);
                        else if (pi.getX() > x)
                            level.playerList.get(pNumber).movement(Direction.EAST);
                        else if (pi.getX() < x)
                            level.playerList.get(pNumber).movement(Direction.WEST);
                    } else {

                        try {
                            this.placeBomb((int) this.getX() / 16, (int) this.getY() / 16);
                        } catch (SlickException e) {
                            e.printStackTrace();
                        }
                        standby = true;
                        deltaSoFar = 0;
                        isSissyBitch = true;
                    }
                }
            }
        }

    }

}
