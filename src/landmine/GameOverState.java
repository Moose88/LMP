package landmine;

import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

import java.util.Iterator;

public class GameOverState extends BasicGameState {

    private int timer;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {

        timer = 4000;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

        LandMineGame lmg = (LandMineGame) game;

        g.drawImage(ResourceManager.getImage(LandMineGame.GAMEOVER_BANNER_RSC), lmg.ScreenWidth/2-ResourceManager.getImage(LandMineGame.GAMEOVER_BANNER_RSC).getWidth()/2, lmg.ScreenHeight/2-ResourceManager.getImage(LandMineGame.GAMEOVER_BANNER_RSC).getHeight()/2);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {


        timer -= delta;
        if (timer <= 0)
            game.enterState(LandMineGame.STARTUPSTATE, new EmptyTransition(), new FadeInTransition());


    }

    @Override
    public int getID() {

        return LandMineGame.GAMEOVERSTATE;
    }

}
