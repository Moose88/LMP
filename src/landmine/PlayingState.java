package landmine;


import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

public class PlayingState extends BasicGameState {

    private int x, y;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        LandMineGame bg = (LandMineGame)game;
        bg.levels.new_level();
        x = 1;
        y = 1;

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

        g.scale(6.25f,6.25f);
        g.fillRect(x*16, y*16, 16, 16);
        g.resetTransform();

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        Input input = container.getInput();
        LandMineGame lmg = (LandMineGame)game;

        if(input.isKeyPressed(Input.KEY_RIGHT)){
            if(lmg.levels.wall(x+1,y) == true){
                x++;
            }
        } else if(input.isKeyPressed(Input.KEY_LEFT)){
            if(lmg.levels.wall(x-1,y) == true){
                x--;
            }
        } else if(input.isKeyPressed(Input.KEY_UP)) {
            if (lmg.levels.wall(x, y-1) == true) {
                y--;
            }
        } else if(input.isKeyPressed(Input.KEY_DOWN)){
            if(lmg.levels.wall(x,y+1) == true){
                y++;
            }
        }

        if (input.isKeyDown(Input.KEY_SPACE))
            lmg.enterState(LandMineGame.GAMEOVERSTATE, new EmptyTransition(), new HorizontalSplitTransition());



    }

    @Override
    public int getID() {
        return LandMineGame.PLAYINGSTATE;
    }

}
