package algos;

import main.Ex3Main_3;
import server.PacManAlgo;
import server.PacmanGame;

public class ManualAlgo implements PacManAlgo {

    /**
     * Default constructor.
     */
    public ManualAlgo() {;}

    /**
     * Returns a short description of this algorithm.
     *
     * @return a String describing the manual control algorithm
     */
    @Override
    public String getInfo() {
        return "This is a manual algorithm for manual controlling the PacMan using w,s,x,d (up,left,down,right).";
    }

    /**
     * Determines the next move of the Pac-Man based on user keyboard input.
     *
     * @param game the current Pac-Man game instance
     * @return an integer representing the movement direction.
     */
    @Override
    public int move(PacmanGame game) {
        int ans = PacmanGame.ERR;

        // Read the last keyboard command
        Character cmd = Ex3Main_3.getCMD();
        if (cmd != null) {
            if (cmd == 'w') {ans = PacmanGame.UP;}
            if (cmd == 's') {ans = PacmanGame.DOWN;}
            if (cmd == 'a') {ans = PacmanGame.LEFT;}
            if (cmd == 'd') {ans = PacmanGame.RIGHT;}

            // Clear the command after it was processed
            Ex3Main_3.clearCMD();
        }
        return  ans;
    }
}
