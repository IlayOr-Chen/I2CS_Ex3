package server;

public interface PacManAlgo {
    /**
     *  Add a short description for the algorithm as a String.
     */
    public String getInfo();

    /**
     * Determines the next move of the Pac-Man based on the algorithm.
     *
     * @param game the current Pac-Man game instance
     * @return an integer representing the movement direction.
     */
    public int move(PacmanGame game);
}
