package landmine;

import jig.Vector;
import org.lwjgl.Sys;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

public class Level {

    private TiledMap map;
    private int wallLayer;
    private static Level instance = null;
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

    private Level(){


    }

    public void new_level(){
        try {
            map = new TiledMap(LandMineGame.LEVEL_RSC);
        } catch (org.newdawn.slick.SlickException e){
            e.printStackTrace();
        }

        for(int i = 0; i < 1; i++){
            try {
                // Manually setting to 24, 24. Instead use mathematical double for loop for player placement
                playerList.add(i, new Person((i+1)*16+8,(i+1)*16+8, i));
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }


    }

    public boolean explosionPath(Vector vector){

        for(Person player : playerList){
            //System.out.println("Player position: " + player.getPosition() + " Explosion vector: " + vector);
           if(player.getPosition().epsilonEquals(vector, 8.0)) {
               player.takeLife();
               //System.out.println("Lives remaining for player " + player.pNumber + " equals: " + player.lives);
               return true;
           }
        }

        for(Bomb bomb : bombList){
            if(bomb.getPosition().epsilonEquals(vector, 1.0)){
                bomb.detonate();
            }
        }



        return false;
    }

    public void checkDeath(){
        for(Person player: playerList){
            if(player.isDead()){
                playerList.remove(player);
            }
        }
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
            instance = new Level();
        }

        return instance;
    }
}
