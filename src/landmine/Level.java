package landmine;

import jig.Vector;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Level {

    private TiledMap map;
    private int wallLayer;
    private static Level instance = null;
    public Music game_theme;
    public Music start_theme;
    public int pNumber;
    private boolean isPlayerBomb = false;
    public int[][] safespace = new int[15][13];
    LinkedList<Vector> safe = new LinkedList<>();

    public ArrayList<Person> playerList = new ArrayList<>(4);

    // List containing explosion positions, and checking if players are contained in said positions
    // Needed for rendering and mechanics implementation
    public ArrayList<Bomb> bombList = new ArrayList<>();

    private Level() throws SlickException {

        game_theme = new Music(LandMineGame.GAMESONG_RSC);
        start_theme = new Music(LandMineGame.PLAYSONG_RSC);

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
            playerList.add(1, new Dinky(13*(16)+8, 24, 1));
            playerList.add(2, new Dinky(16+8, 11*(16)+8, 2));
            playerList.add(3, new Dinky(13*(16)+8, 11*(16)+8, 3));

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

    public void findSafeSpaces(){
        // First zero safe space array so everything is safe and warm
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 13; j++){
                if(notAWall(i, j))
                    safespace[i][j] = 1;
                else
                    safespace[i][j] = Integer.MAX_VALUE;
            }
        }

        // for each bomb in list update safe spaces
        for( Bomb bomb : bombList){
            int x = bomb.gridX;
            int y = bomb.gridY;

            int ix = x;
            int iy = y;

            while(notAWall(ix, iy)){
                safespace[ix][iy]++;
                iy--;
            }

            ix = x;
            iy = y;

            while(notAWall(ix, iy)){
                safespace[ix][iy]++;
                iy++;
            }

            ix = x;
            iy = y;

            while(notAWall(ix, iy)){
                safespace[ix][iy]++;
                ix--;
            }

            ix = x;
            iy = y;

            while(notAWall(ix, iy)){
                safespace[ix][iy]++;
                ix++;
            }


        }

        for(int i = 0; i < safespace.length; i++){
            for(int j = 0; j < safespace[i].length; j++){
                System.out.print(String.format(" %s ",safespace[i][j]).replace("], ", "]\n"
                ).replace("[[", "["
                ).replace("]]", "]"
                ).replace(", ", " "
                ).replace(String.valueOf(Integer.MAX_VALUE),"W"
                ).replace("0", " "));
            }
            System.out.println();
        }


        safe = new LinkedList<>();
        for(int i = 0; i < safespace.length; i++){
            for(int j = 0; j < safespace[i].length; j++){
                if(safespace[i][j] == 1){
                    safe.add(new Vector(i,j));
                }
            }
        }
        System.out.println("Source Vector Array is " + safe.size() + " long");
        Vector[] s = safe.toArray(new Vector[safe.size()]);
        dykeStraw.getSafeInstance().perform(safespace, s);

    }

}
