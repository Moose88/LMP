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
    private boolean success;
    public static int score;
    private Person player;
    private Person comp1;
    private Person comp2;
    private Person comp3;
    public static int second;
    public static int deltaSoFar;
    public static int waitTime;
    public static int dead;
    public float volume;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        score = 0;
        paused = false;
        success = false;
        newLevel = false;
        waitTime = 0;

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        Input input = container.getInput();

        second = 1000;
        dead = 0;
        deltaSoFar = 0;
        success = false;
        newLevel = false;
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
        g.drawString("Score: " + score, 120, 675);
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

            g.pushTransform();
            g.scale(2f,2f);

            String controls = "Controls:\n Up/Down/Left/Right: Directional keys\n Place Bomb: Space\n Pause: ESC\n Quit: 'Q'";

            g.setColor(new Color(0.5f,0.0f,0.0f,0.5f));

            float x2 = ((lmg.ScreenWidth/10f)-(g.getFont().getWidth(controls)/3f));
            float y2 = ((lmg.ScreenHeight/16f)-(g.getFont().getHeight(controls)/3f));

            g.fillRect(x2,y2,g.getFont().getWidth(controls)+10,g.getFont().getHeight(controls)+10);
            g.setColor(Color.cyan);
            g.drawString(controls, x2+3, y2+3);
            g.popTransform();
        }

        if(success){
            g.scale(3f,3f);

            String nextlevel = "SUCCESS!!!\nYou've survived this round, now survive the next!";

            g.setColor(new Color(0.5f,0.0f,0.0f,0.5f));

            float x1 = ((lmg.ScreenWidth/8.7f)-(g.getFont().getWidth(nextlevel)/3f));
            float y1 = ((lmg.ScreenHeight/6f)-(g.getFont().getHeight(nextlevel)/3f));

            g.fillRect(x1,y1,g.getFont().getWidth(nextlevel)+10,g.getFont().getHeight(nextlevel)+10);
            g.setColor(Color.cyan);
            g.drawString(nextlevel, x1+3, y1+3);
            g.resetTransform();
        }

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
        LandMineGame lmg = (LandMineGame)game;

        deltaSoFar += delta;

        if(deltaSoFar >= second){
            if(!newLevel)
                score = score + 100;
            deltaSoFar = 0;
        }

        if(paused) {
            input.clearKeyPressedRecord();
            level.start_theme.setVolume(volume/10f);
            return;
        }

        if(newLevel){
            System.out.println("Next level!");
            //Render a success screen for 5 seconds, then do whatever.
            success = true;
            if(waitTime >= 5000)
                lmg.enterState(LandMineGame.PLAYINGSTATE, new EmptyTransition(), new HorizontalSplitTransition());

            waitTime += delta;
        }

        if(player.lives == 0) {
            player.getDeath().stop();
            //input.clearKeyPressedRecord();
            //lmg.enterState(LandMineGame.GAMEOVERSTATE, new EmptyTransition(), new HorizontalSplitTransition());
        }

        int dead = 0;
        for(Person person : level.playerList){
            if(person.isDead()) {
                System.out.println("Someone died lul, dead = " + dead);
                dead++;
            }

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
            score--;

        } else if(input.isKeyDown(Input.KEY_LEFT)){

            player.movement(Person.Direction.WEST);
            score--;

        } else if(input.isKeyDown(Input.KEY_UP)) {

            player.movement(Person.Direction.NORTH);
            score--;

        } else if(input.isKeyDown(Input.KEY_DOWN)){

            player.movement(Person.Direction.SOUTH);
            score--;

        }

        if (input.isKeyPressed(Input.KEY_SPACE) ) {
            player.placeBomb((int) player.getX()/16, (int) player.getY()/16);
            score = score+50;
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
            level.start_theme.setVolume(volume * 10f);
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
