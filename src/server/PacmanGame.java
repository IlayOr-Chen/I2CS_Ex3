package server;

public interface PacmanGame {
    int INIT = 0;
    int PLAY = 1;
    int DONE = 3;
    int LOSE = -2;
    int ERR = -1;
    int STAY = 0;
    int LEFT = 2;
    int RIGHT = 4;
    int UP = 1;
    int DOWN = 3;

    /** Returns PacMan's current position as a string "x,y" */
    public String getPos(int id);

    /** Returns the array of ghosts */
    public GhostCL[] getGhosts(int id);

    /** Returns the current board as a 2D int array */
    public int[][] getGame(int id);

    /**
     * Moves PacMan in the specified direction and updates ghosts.
     * Handles scoring, eatable ghosts, and collisions.
     * @param dir direction of PacMan (UP, DOWN, LEFT, RIGHT)
     */
    public void move(int dir);

    /** Starts the game by setting status to PLAY */
    public void play();

    /** Ends the game by setting status to DONE */
    public void end(int id);

    /** Returns the current game status */
    public int getStatus();

    /**
     * Initializes the game with parameters
     * @param playerId player identifier
     * @param cyclicMode whether the map wraps around edges
     */
    public void init(String playerId, boolean cyclicMode);
}
