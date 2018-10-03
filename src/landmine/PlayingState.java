package landmine;


import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

public class PlayingState extends BasicGameState {
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        LandMineGame bg = (LandMineGame)game;
        bg.levels.new_level();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(true);
    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        LandMineGame lmg = (LandMineGame)game;

        //Map Render

        g.scale(6.25f,6.25f);
        lmg.levels.render();
        g.resetTransform();

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        Input input = container.getInput();
        LandMineGame bg = (LandMineGame)game;

        if (input.isKeyDown(Input.KEY_SPACE))
            bg.enterState(LandMineGame.GAMEOVERSTATE, new EmptyTransition(), new HorizontalSplitTransition());



    }

    @Override
    public int getID() {
        return LandMineGame.PLAYINGSTATE;
    }

}
