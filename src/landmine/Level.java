package landmine;

import org.newdawn.slick.tiled.TiledMap;

public class Level {

    private TiledMap map;
    private int wallLayer;
    private static Level instance = null;

    public Level(){


    }

    public void new_level(){
        try {
            map = new TiledMap(LandMineGame.LEVEL_RSC);
        } catch (org.newdawn.slick.SlickException e){
            e.printStackTrace();
        }
    }

    public boolean notAWall(int x, int y){
        wallLayer = map.getLayerIndex("Walls");
        System.out.println("wallLayer: " + wallLayer);
        map.getTileId(0, 0, wallLayer);

        System.out.println("TileID: " + map.getTileId(x, y, wallLayer));

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
