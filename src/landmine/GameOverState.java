package landmine;

import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;

public class GameOverState extends BasicGameState {

    private int timer;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        container.setShowFPS(false);
        Input input = container.getInput();
        input.clearKeyPressedRecord();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        Input input = container.getInput();
        input.clearKeyPressedRecord();
        timer = 4000;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        LandMineGame lmg = (LandMineGame) game;

        g.drawImage(ResourceManager.getImage(LandMineGame.GAMEOVER_BANNER_RSC),
                lmg.ScreenWidth/2f-ResourceManager.getImage(LandMineGame.GAMEOVER_BANNER_RSC).getWidth()/2f,
                lmg.ScreenHeight/2f-ResourceManager.getImage(LandMineGame.GAMEOVER_BANNER_RSC).getHeight()/2f);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();

        timer -= delta;
        if (timer <= 0) {
            input.resetInputTransform();
            input.clearKeyPressedRecord();
            game.enterState(LandMineGame.STARTUPSTATE, new EmptyTransition(), new FadeInTransition());
        }

        if(input.isKeyPressed(Input.KEY_Q)){
            container.exit();
        }


    }

    @Override
    public int getID() {
        return LandMineGame.GAMEOVERSTATE;
    }

}
