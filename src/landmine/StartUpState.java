package landmine;

import jig.ResourceManager;

import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

/**
 * This state is active prior to the Game starting. In this state, sound is
 * turned off, and the bounce counter shows '?'. The user can only interact with
 * the game by pressing the SPACE key which transitions to the Playing State.
 * Otherwise, all game objects are rendered and updated normally.
 *
 * Transitions From (Initialization), GameOverState
 *
 * Transitions To PlayingState
 */

class StartUpState extends BasicGameState {

    private Image banner;
    private int rotation = 0;
    public Level level = Level.getInstance();


    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        LandMineGame lmg = (LandMineGame)game;

        Input input = container.getInput();
        input.clearKeyPressedRecord();



        //ResourceManager.getSound(LandMineGame.GAMESONG_RSC).loop(1, -5);

        // Start banner and initialize rotation center
        banner = new Image("landmine/resource/banner.png");
        banner.setCenterOfRotation(lmg.ScreenWidth, lmg.ScreenHeight);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {

        // Start game music
        level.game_theme.loop();

        container.setSoundOn(true);
        Input input = container.getInput();
        input.clearKeyPressedRecord();
        PlayingState.remainingLives = 3;
    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        LandMineGame lmg = (LandMineGame)game;

        //Background Flag
        Color kelly  = new Color(74,129,35);
        Color lavender = new Color(181,126,220);

        g.setColor(lavender);
        g.fillRect(0,0,lmg.ScreenWidth, lmg.ScreenHeight/3f);
        g.setColor(Color.white);
        g.fillRect(0,lmg.ScreenHeight/3f, lmg.ScreenWidth, (lmg.ScreenHeight*2)/3f);
        g.setColor(kelly);
        g.fillRect(0, (2*lmg.ScreenHeight/3f),lmg.ScreenWidth, lmg.ScreenHeight);


        // Press Start image and Rotating Banner

        g.drawImage(ResourceManager.getImage(LandMineGame.BANNER_BACK_RSC), lmg.ScreenWidth/2f,
                lmg.ScreenHeight/2f);
        g.drawImage(banner, (lmg.ScreenWidth/2f), (lmg.ScreenHeight/2f));
        g.drawImage(ResourceManager.getImage(LandMineGame.STARTUP_BANNER_RSC),
                lmg.ScreenWidth/2f-(ResourceManager.getImage(LandMineGame.STARTUP_BANNER_RSC).getWidth()/2f),
                lmg.ScreenHeight/2f+(ResourceManager.getImage(LandMineGame.STARTUP_BANNER_RSC).getHeight()/2f));


        //Name on the splash
        g.setColor(Color.white);
        g.pushTransform();
        g.scale(2f, 2f);
        g.drawString("By Matthew Bourgoine\nHigh Score: " + lmg.highScore, 530, 600);
        g.popTransform();

        g.pushTransform();
        g.scale(2f,2f);

        String controls = "Controls:\n Up/Down/Left/Right: Directional keys\n Place Bomb: Space\n Pause: ESC\n Quit: 'Q'";

        g.setColor(lavender);

        float x1 = ((lmg.ScreenWidth/11.5f)-(g.getFont().getWidth(controls)/3f));
        float y1 = ((lmg.ScreenHeight/16f)-(g.getFont().getHeight(controls)/3f));

        g.fillRect(x1,y1,g.getFont().getWidth(controls)+10,g.getFont().getHeight(controls)+10);
        g.setColor(Color.white);
        g.drawString(controls, x1+3, y1+3);
        g.popTransform();

        g.pushTransform();
        g.scale(2f,2f);

        String scoring = "Scoring:\n Moving: -1 each step .\n Each second you survive: +100.\n Placing a bomb: +50.\n Taking a hit: -200.";

        g.setColor(lavender);

        float x2 = ((lmg.ScreenWidth/2.8f)-(g.getFont().getWidth(scoring)/3f));
        float y2 = ((lmg.ScreenHeight/16f)-(g.getFont().getHeight(scoring)/3f));

        g.fillRect(x2,y2,g.getFont().getWidth(scoring)+10,g.getFont().getHeight(scoring)+10);
        g.setColor(Color.white);
        g.drawString(scoring, x2+3, y2+3);
        g.popTransform();

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
        LandMineGame lmg = (LandMineGame)game;

        // Rotation
        rotation -= delta/15;
        banner.setRotation(rotation);

        // Checks for proper input
        if (input.isKeyPressed(Input.KEY_SPACE)) {
            lmg.enterState(LandMineGame.PLAYINGSTATE, new EmptyTransition(), new HorizontalSplitTransition());
            level.game_theme.stop();
            level.start_theme.play();
            input.clearKeyPressedRecord();
        }

        if(input.isKeyPressed(Input.KEY_Q)){
            container.exit();
        }

    }

    @Override
    public int getID() {
        return LandMineGame.STARTUPSTATE;
    }

}