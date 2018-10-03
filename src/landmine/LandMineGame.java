package landmine;

import java.util.ArrayList;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class LandMineGame extends StateBasedGame {

    public static final int STARTUPSTATE = 0;
    public static final int PLAYINGSTATE = 1;
    public static final int GAMEOVERSTATE = 2;

    public static final String STARTUP_BANNER_RSC = "landmine/resource/PressSpace.png";
    public static final String GAMEOVER_BANNER_RSC = "landmine/resource/GameOver.png";
    public static final String BACKGROUND_RSC = "landmine/resource/background.png";
    public static final String GAMESONG_RSC = "landmine/resource/Gamesong.wav";
    public static final String LEVEL_RSC = "landmine/resource/map.tmx";
    public static final String PDOWN_RSC = "landmine/resource/character/main_d.png";

    public Level levels;

    public final int ScreenWidth;
    public final int ScreenHeight;

    /**
     * Create the BounceGame frame, saving the width and height for later use.
     *
     * @param title
     *            the window's title
     * @param width
     *            the window's width
     * @param height
     *            the window's height
     */
    public LandMineGame(String title, int width, int height) throws SlickException {
        super(title);
        ScreenHeight = height;
        ScreenWidth = width;

        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
        Entity.setDebug(true);

    }


    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new landmine.StartUpState());
        addState(new GameOverState());
        addState(new PlayingState());

        // the sound resource takes a particularly long time to load,
        // we preload it here to (1) reduce latency when we first play it
        // and (2) because loading it will load the audio libraries and
        // unless that is done now, we can't *disable* sound as we
        // attempt to do in the startUp() method.
        // ResourceManager.loadSound(BANG_EXPLOSIONSND_RSC);
        ResourceManager.loadSound(GAMESONG_RSC);

        // preload all the resources to avoid warnings & minimize latency...
//        ResourceManager.loadImage(BALL_BALLIMG_RSC);
//        ResourceManager.loadImage(BALL_BROKENIMG_RSC);
        ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
        ResourceManager.loadImage(STARTUP_BANNER_RSC);
        ResourceManager.loadImage(PDOWN_RSC);
//        ResourceManager.loadImage(BLOCK_SPRITE_RSC);
//        ResourceManager.loadImage(BALL_PADDLE_RSC);
        ResourceManager.loadImage(BACKGROUND_RSC);
//        ResourceManager.loadImage(GAME_BACKGROUND_RSC);

        levels = new Level();




    }

    public static void main(String[] args) {
        AppGameContainer app;
        try {
            app = new AppGameContainer(new LandMineGame("Landmine Person!", 1500, 1300));
            app.setDisplayMode(1500, 1300, false);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }


}
