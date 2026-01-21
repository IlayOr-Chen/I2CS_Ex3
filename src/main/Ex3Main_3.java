package main;

import server.*;
import utils.Sound;

/**
 * Ex3, School of Computer Science, Ariel University.
 *
 * This is the "main" class for Ex3.
 * Do NOT change this class!!
 *
 * Basic roles:
 * 1. Space bar starts the game (and pause it).
 * 2. 'c' changes the cyclic mode (default is true).
 * 3. In manual mode: 'w'-up, 'a'-left, 'x'-down, 'd'-right.
 * 4. The Game (and the Gamer) parameters are defined in the Info class.
 * 4. Your are asked to implement the following classes: utils.Index2D, utils.Map, algos.Ex3Algo.
 * 5. Keep in mind that in order to implement this assignment - you might want to implement few additional classes (on top of adding JUnit classes).
 * 6. The dame has 5 main "levels" ([0,4]). You are request to run&test them all.
 * 7. After each run, the system prints (in the terminal, in red) a String with your game results -
 * you are asked to upload your results (at least one for each level), part of your grade will be based on those results.
 *
 */
public class Ex3Main_3 {
    private static Character _cmd;

    public static void main(String[] args) {
        play1();
    }

    /**
     * Initializes and runs a single game session.
     */
    public static void play1() {
    	Game game = new Game();

        // Initialize the game using predefined parameters from GameInfo
        game.init(GameInfo.MY_ID, GameInfo.CYCLIC_MODE);

        // Create the game GUI and show the start screen
        GameGui gui = new GameGui(game);
        gui.showStartScreen();

        // Start the game engine
        game.play();

        // Choose algorithm: automatic or manual
        PacManAlgo man;
        if(gui.isAutomatic())
            man = GameInfo.MY_ALGO;
        else
            man = GameInfo.MANUAL_ALGO;

        // Start background music
        Sound.playLoop("resources/pacman_sound.wav");

        // Main game loop - runs until the game is finished
        while(game.getStatus()!= PacmanGame.DONE) {
            // Read keyboard input only in manual mode
            if(!gui.isAutomatic() && StdDraw.hasNextKeyTyped())
                _cmd = StdDraw.nextKeyTyped();

            // Ask the algorithm for the next move
            int dir = man.move(game);

            // Apply the move to the game
            game.move(dir);

            // Redraw the game board
            gui.drawMap();
            StdDraw.pause(60);
        }

        // Stop background music when the game ends
        Sound.stop();

        // End the game and print results
        game.end(-1);
    }

    /**
     * Returns the last keyboard command entered by the user.
     *
     * @return the current command character, or null if none exists
     */
    public static Character getCMD() {return _cmd;}

    /**
     * Clears the stored keyboard command.
     */
    public static void clearCMD() {_cmd = null;}

}
