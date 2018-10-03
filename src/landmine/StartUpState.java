package landmine;

import java.util.Iterator;

import jig.ResourceManager;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;
import org.newdawn.slick.state.transition.Transition;

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



    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        ResourceManager.getSound(LandMineGame.GAMESONG_RSC).loop();
        LandMineGame bg = (LandMineGame)game;
        bg.levels.new_level();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(true);
    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        LandMineGame bg = (LandMineGame)game;

        //Background
        g.drawImage(ResourceManager.getImage(LandMineGame.BACKGROUND_RSC),-500,-50);

        g.scale(6.25f,6.25f);
        bg.levels.render();
        g.resetTransform();


        g.drawImage(ResourceManager.getImage(LandMineGame.STARTUP_BANNER_RSC), bg.ScreenWidth/2-(ResourceManager.getImage(LandMineGame.STARTUP_BANNER_RSC).getWidth()/2), bg.ScreenHeight/2+(ResourceManager.getImage(LandMineGame.STARTUP_BANNER_RSC).getHeight()/2));

        //Name on the splash
        g.setColor(Color.white);
        g.drawString("By Matthew Bourgoine", 800, 970);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        Input input = container.getInput();
        LandMineGame bg = (LandMineGame)game;

        if (input.isKeyDown(Input.KEY_SPACE))
            bg.enterState(LandMineGame.PLAYINGSTATE, new EmptyTransition(), new HorizontalSplitTransition());



    }

    @Override
    public int getID() {
        return LandMineGame.STARTUPSTATE;
    }

}