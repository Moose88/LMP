package landmine;

import org.newdawn.slick.tiled.TiledMap;

public class Level {

    private TiledMap map;

    public Level(){
    }

    public void new_level(){
        try {
            map = new TiledMap(LandMineGame.LEVEL_RSC);
        } catch (org.newdawn.slick.SlickException e){
            e.printStackTrace();
        }
    }

    public void render(){
        map.render(0,0);
    }
}
