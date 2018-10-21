package landmine;


import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;


public class PlayingState extends BasicGameState {

    private Level level = Level.getInstance();

    private boolean paused;
    private boolean newLevel;
    private Person player;
    private Person comp1;
    private Person comp2;
    private Person comp3;
    public float volume;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        paused = false;
        newLevel = false;

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        Input input = container.getInput();

        level.playerList.clear();
        level.bombList.clear();
        level.new_level();
        level.findSafeSpaces();
        volume = level.game_theme.getVolume();
        player = level.playerList.get(0);
        comp1 = level.playerList.get(1);
        comp2 = level.playerList.get(2);
        comp3 = level.playerList.get(3);
        level.pNumber = player.pNumber;

        container.setSoundOn(true);
        input.clearKeyPressedRecord();
        if(!paused){
            level.bombList.clear();
        }

    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        LandMineGame lmg  = (LandMineGame)game;

        // Map Render
        g.pushTransform();
        g.scale(6.25f,6.25f);
        level.render();

        // Player object
        for(Person player : level.playerList) {
            if(player.lives > 0) {
                player.render(g);
            }
        }

        g.popTransform();

        // Bottom menu
        g.pushTransform();
        g.scale(2,2);
        g.drawString("Lives: " + level.playerList.get(0).lives, 20, 675);
        g.drawString("Score: " + player.score, 120, 675);
        if(comp1 != null)
            g.drawString("Player 2 lives: " + comp1.lives, 250, 650);
        if(comp2 != null)
            g.drawString("Player 3 lives: " + comp2.lives, 250, 675);
        if(comp3 != null)
            g.drawString("Player 4 lives: " + comp3.lives, 250, 700);
        g.popTransform();

        // Paused menu
        if(paused){
            g.scale(3f,3f);

            String paused = "PAUSED!";

            g.setColor(new Color(0.5f,0.0f,0.0f,0.5f));

            float x1 = ((lmg.ScreenWidth/6f)-(g.getFont().getWidth(paused)/3f));
            float y1 = ((lmg.ScreenHeight/6f)-(g.getFont().getHeight(paused)/3f));

            g.fillRect(x1,y1,g.getFont().getWidth(paused)+10,g.getFont().getHeight(paused)+10);
            g.setColor(Color.cyan);
            g.drawString(paused, x1+3, y1+3);
            g.resetTransform();
        }

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
        LandMineGame lmg = (LandMineGame)game;

        if(paused) {
            level.game_theme.setVolume(volume/10f);
            return;
        }

        if(newLevel){
            System.out.println("Next level!");
            lmg.enterState(LandMineGame.PLAYINGSTATE, new EmptyTransition(), new HorizontalSplitTransition());
        }

        if(player.lives == 0) {
            player.getDeath().stop();
            input.clearKeyPressedRecord();
            lmg.enterState(LandMineGame.GAMEOVERSTATE, new EmptyTransition(), new HorizontalSplitTransition());
        }

        for(Person person : level.playerList){
            int dead = 0;
            if(person.isDead())
                dead++;

            if(dead == 3){
                newLevel = true;
            }
        }

        for(Person person : level.playerList){
            if(person != null)
                person.update(delta);
        }

        for (Bomb bomb : level.bombList) {
            bomb.update(delta);
        }

        if(input.isKeyDown(Input.KEY_RIGHT)){

            player.movement(Person.Direction.EAST);
            player.score++;

        } else if(input.isKeyDown(Input.KEY_LEFT)){

            player.movement(Person.Direction.WEST);
            player.score++;

        } else if(input.isKeyDown(Input.KEY_UP)) {

            player.movement(Person.Direction.NORTH);
            player.score++;

        } else if(input.isKeyDown(Input.KEY_DOWN)){

            player.movement(Person.Direction.SOUTH);
            player.score++;

        }

        if (input.isKeyPressed(Input.KEY_SPACE) ) {
            player.placeBomb((int) player.getX()/16, (int) player.getY()/16);
        }

        if(input.isKeyPressed(Input.KEY_Q)){
            container.exit();
        }


    }

    @Override
    public void keyPressed(int key, char c) {
        super.keyPressed(key, c);

        //System.out.println("Key pressed: " + key);
        if(key == Input.KEY_ESCAPE ) {
            level.game_theme.setVolume(volume * 10f);
            paused = !paused;
        }

        if(key == Input.KEY_Z){
            player.lives = 0;
            player.takeLife();
        }
    }

    @Override
    public int getID() {
        return LandMineGame.PLAYINGSTATE;
    }

}
