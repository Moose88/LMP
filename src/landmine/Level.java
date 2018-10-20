package landmine;

import jig.Vector;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.Iterator;

public class Level {

    private TiledMap map;
    private int wallLayer;
    private static Level instance = null;
    Music game_theme;
    public int pNumber;
    public Vector position;


    // List containing player positions
    public ArrayList<Vector> locationList = new ArrayList<>();

    public ArrayList<Person> playerList = new ArrayList<>(4);

    // List containing bomb positions (for rendering)
    public ArrayList<Explosion> explisionList = new ArrayList<>();

    // List containing explosion positions, and checking if players are contained in said positions
    // Needed for rendering and mechanics implementation
    public ArrayList<Bomb> bombList = new ArrayList<>();

    private Level() throws SlickException {

        game_theme = new Music(LandMineGame.GAMESONG_RSC);

    }

    public void new_level(){
        try {
            map = new TiledMap(LandMineGame.LEVEL_RSC);
        } catch (org.newdawn.slick.SlickException e){
            e.printStackTrace();
        }

        try {
            // Manually setting to 24, 24. Instead use mathematical double for loop for player placement
            playerList.add(0, new Person(16+8,16+8, 0));
            playerList.add(1, new Person(13*(16)+8, 24, 1));
            playerList.add(2, new Person(16+8, 11*(16)+8, 2));
            playerList.add(3, new Person(13*(16)+8, 11*(16)+8, 3));
        } catch (SlickException e) {
            e.printStackTrace();
        }



    }

    public boolean explosionPath(Vector vector){

        for(Bomb bomb : bombList){
            if(bomb.getPosition().epsilonEquals(vector, 8.0)){
                bomb.detonate();
            }
        }

        for(Person player : playerList){
            //System.out.println("Player position: " + player.getPosition() + " Explosion vector: " + vector);
            if(player.getPosition().epsilonEquals(vector, 8.0)) {
               player.takeLife();
               //System.out.println("Lives remaining for player " + player.pNumber + " equals: " + player.lives);
               return true;
           }
        }

        return false;
    }

    public boolean isBombHere(Vector vector){
        for(Bomb bomb : bombList){
            System.out.println("Bomb position: " + bomb.getPosition() + " vector position: " + vector);
            if(bomb.getPosition().epsilonEquals(vector, 8.0))
                return true;
        }

        return false;
    }

    public boolean checkDeath(){
        Iterator<Person> it = playerList.iterator();
        while(it.hasNext()){
            Person person = it.next();
            if(person.isDead()){
                if(person.pNumber == pNumber){
                    return true;
                }
                it.remove();
            }
        }
        return false;
    }

    public boolean notAWall(int x, int y){
        wallLayer = map.getLayerIndex("Walls");
        //System.out.println("wallLayer: " + wallLayer);
        map.getTileId(0, 0, wallLayer);
        //System.out.println("TileID: " + map.getTileId(x, y, wallLayer));

        if(map.getTileId(x, y, wallLayer) == 0){
            return true;
        }

        return false;

    }

    public void render(){
        map.render(0,0);

    }

    public static Level getInstance(){
        if(instance == null){
            try {
                instance = new Level();
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }
}
