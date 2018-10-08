package landmine;


import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

public class PlayingState extends BasicGameState {

    private int x, y;
    private int lives;
    private int score;
    private boolean paused;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        LandMineGame lmg = (LandMineGame)game;
        lmg.levels.new_level();

        lives = 3;
        score = 0;
        paused = false;
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


        // Map Render
        g.scale(6.25f,6.25f);
        lmg.levels.render();
        g.resetTransform();

        // Top menu
        g.scale(2,2);
        g.drawString("Lives: " + lives, 20, 20);
        g.drawString("Score: " + score, 120, 20);
        g.resetTransform();

        // Player object
        g.scale(6.25f,6.25f);
        g.fillRect(x*16, y*16+20, 16, 16);
        g.resetTransform();

        // Paused menu
        if(paused){
            g.scale(3f,3f);

            String paused = "PAUSED!";

            g.setColor(new Color(0.5f,0.0f,0.0f,0.5f));

            float x1 = ((lmg.ScreenWidth/6)-(g.getFont().getWidth(paused)/3));
            float y1 = ((lmg.ScreenHeight/6)-(g.getFont().getHeight(paused)/3));

            g.fillRect(x1,y1,g.getFont().getWidth(paused)+10,g.getFont().getHeight(paused)+10);
            g.setColor(Color.cyan);
            g.drawString(paused, x1+3, y1+3);
            g.resetTransform();
        }

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        if(paused)
            return;

        Input input = container.getInput();
        LandMineGame lmg = (LandMineGame)game;

        if(input.isKeyDown(Input.KEY_RIGHT)){
            if(lmg.levels.wall(x+1,y) == true){


                x++;
                score++;
                System.out.println("Moving Right");
            }
        } else if(input.isKeyDown(Input.KEY_LEFT)){
            if(lmg.levels.wall(x-1,y) == true){
                x--;
                score++;
                System.out.println("Moving Left");
            }
        } else if(input.isKeyDown(Input.KEY_UP)) {
            if (lmg.levels.wall(x, y-1) == true) {
                y--;
                score++;
                System.out.println("Moving Up");
            }
        } else if(input.isKeyDown(Input.KEY_DOWN)){
            if(lmg.levels.wall(x,y+1) == true){
                y++;
                score++;
                System.out.println("Moving Down");
            }
        }

        if (input.isKeyDown(Input.KEY_SPACE))
            lmg.enterState(LandMineGame.GAMEOVERSTATE, new EmptyTransition(), new HorizontalSplitTransition());



    }

    @Override
    public void keyPressed(int key, char c) {
        super.keyPressed(key, c);
        System.out.println("Key pressed: " + key);
        if(key == Input.KEY_ESCAPE )
            paused = !paused;

    }

    @Override
    public int getID() {
        return LandMineGame.PLAYINGSTATE;
    }

}
