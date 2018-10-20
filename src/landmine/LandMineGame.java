package landmine;

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
    public static final String MASTER_RSC = "landmine/resource/bomb_party_v4.png";
    public static final String BANNER_RSC = "landmine/resource/banner.png";
    public static final String BANNER_BACK_RSC = "landmine/resource/banner background.png";
    public static final String PERSON_HIT_RSC = "landmine/resource/hit.wav";
    public static final String[] EXPLOSION_ARR = {
            "landmine/resource/explosion sounds/Explosion1.wav",
            "landmine/resource/explosion sounds/Explosion2.wav",
            "landmine/resource/explosion sounds/Explosion3.wav",
            "landmine/resource/explosion sounds/Explosion4.wav",
            "landmine/resource/explosion sounds/Explosion5.wav",
            "landmine/resource/explosion sounds/Explosion6.wav",
            "landmine/resource/explosion sounds/Explosion7.wav",
            "landmine/resource/explosion sounds/Explosion8.wav",
            "landmine/resource/explosion sounds/Explosion9.wav",
            "landmine/resource/explosion sounds/Explosion10.wav",
            "landmine/resource/explosion sounds/Explosion11.wav",
            "landmine/resource/explosion sounds/Explosion12.wav",
            "landmine/resource/explosion sounds/Explosion13.wav",
            "landmine/resource/explosion sounds/Explosion14.wav",
            "landmine/resource/explosion sounds/Explosion15.wav",
            "landmine/resource/explosion sounds/Explosion16.wav",
    };




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
        //Entity.setDebug(true);

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
        ResourceManager.loadSound(GAMESONG_RSC);
        ResourceManager.loadSound(PERSON_HIT_RSC);
        for(String string : EXPLOSION_ARR){
            ResourceManager.loadSound(string);
        }

        // preload all the resources to avoid warnings & minimize latency...
        ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
        ResourceManager.loadImage(STARTUP_BANNER_RSC);
        ResourceManager.loadImage(MASTER_RSC);
        ResourceManager.loadImage(BACKGROUND_RSC);
        ResourceManager.loadImage(BANNER_RSC);
        ResourceManager.loadImage(BANNER_BACK_RSC);

    }

    public static void main(String[] args) {
        AppGameContainer app;
        try {
            app = new AppGameContainer(new LandMineGame("Landmine Person!", 1500, 1430));
            app.setDisplayMode(1500, 1430, false);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }


}
