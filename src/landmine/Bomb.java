package landmine;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.concurrent.ThreadLocalRandom;

public class Bomb extends Entity {

    SpriteSheet master = new SpriteSheet(ResourceManager.getImage(LandMineGame.MASTER_RSC), 16, 16);
    private Animation bomb;
    private boolean hasExploded = false;
    public boolean needsDeletion = false;
    public Level level = Level.getInstance();
    int explosion_num = 0;
    public Explosion[] explosions = new Explosion[4];

    public int x;
    public int y;
    public int gridX;
    public int gridY;
    public int timer;

    public Bomb(int x, int y, int timer) throws SlickException {
        super(x, y);

        this.timer = timer;
        this.x = x;
        gridX = x/16;
        this.y = y;
        gridY = y/16;
        bomb = new Animation(master, 4, 18, 9, 18, true, 300, true);
        addAnimation(bomb);
    }

    private int deltaSoFar = 0;
    public void update(int delta){
        deltaSoFar += delta;
        if(!hasExploded) {

            if (timer > 0) {
                if (deltaSoFar >= timer) {
                    hasExploded = true;

                    explosion_num = ThreadLocalRandom.current().nextInt(0, LandMineGame.EXPLOSION_ARR.length);
                    ResourceManager.getSound(LandMineGame.EXPLOSION_ARR[explosion_num]).play();

                    deltaSoFar = 0;

                    removeAnimation(bomb);
                    addImageWithBoundingBox(master.getSubImage(2, 18));
                    if (level.notAWall(gridX, gridY - 1)) {
                        explosions[0] = new Explosion(x, y - 16, Explosion.Direction.NORTH);
                    }
                    if (level.notAWall(gridX, gridY + 1)) {
                        explosions[1] = new Explosion(x, y + 16, Explosion.Direction.SOUTH);
                    }
                    if (level.notAWall(gridX - 1, gridY)) {
                        explosions[2] = new Explosion(x - 16, y, Explosion.Direction.WEST);
                    }
                    if (level.notAWall(gridX + 1, gridY)) {
                        explosions[3] = new Explosion(x + 16, y, Explosion.Direction.EAST);
                    }

                }
            }
        } else {
            if(deltaSoFar > 500){
                needsDeletion = true;
            }
        }

    }

    public void detonate(){
        timer = deltaSoFar;
    }

    @Override
    public void render(Graphics g){
        super.render(g);

        for(int i = 0; i <4; i++){
            if(explosions[i] != null){
                explosions[i].render(g);
            }
        }
    }
}
