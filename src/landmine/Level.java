package landmine;

import org.newdawn.slick.tiled.TiledMap;

public class Level {

    private TiledMap map;
    private int objectLayer;

    public Level(){
    }

    public void new_level(){
        try {
            map = new TiledMap(LandMineGame.LEVEL_RSC);
        } catch (org.newdawn.slick.SlickException e){
            e.printStackTrace();
        }
    }

    public boolean wall(int x, int y){
        objectLayer = map.getLayerIndex("Walls");
        map.getTileId(0, 0, objectLayer);

        if(map.getTileId(x, y, objectLayer) == 0){
            return true;
        }

        return false;

    }

    public void render(){
        map.render(0,0);

    }
}
