package landmine;


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

        lmg.levels = new Level();

        lives = 3;
        score = 0;
        paused = false;
        x = 1;
        y = 1;

        lmg.levels.new_level();
        lmg.player = new Person(x*16+8,y*16+8, lmg.levels);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(true);
    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        LandMineGame lmg = (LandMineGame)game;


        // Map Render
        g.pushTransform();
        g.scale(6.25f,6.25f);
        lmg.levels.render();

        // Player object
        lmg.player.render(g);
        g.popTransform();

        // Bottom menu
        g.pushTransform();
        g.scale(2,2);
        g.drawString("Lives: " + lives, 20, 675);
        g.drawString("Score: " + score, 120, 675);
        g.popTransform();

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

        lmg.player.update(delta);

        if(input.isKeyDown(Input.KEY_RIGHT)){

                lmg.player.movement(Person.Direction.EAST);
                score++;

        } else if(input.isKeyDown(Input.KEY_LEFT)){

                lmg.player.movement(Person.Direction.WEST);
                score++;

        } else if(input.isKeyDown(Input.KEY_UP)) {

                lmg.player.movement(Person.Direction.NORTH);
                score++;

        } else if(input.isKeyDown(Input.KEY_DOWN)){

                lmg.player.movement(Person.Direction.SOUTH);
                score++;

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
