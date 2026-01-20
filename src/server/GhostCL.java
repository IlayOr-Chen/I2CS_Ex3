package server;

public interface GhostCL {
    int INIT = 0;
    int PLAY = 1;
    int PAUSE = 2;
    int RANDOM_WALK0 = 10;
    int RANDOM_WALK1 = 11;
    int GREEDY_SP = 12;

    /**
     * Returns the ghost type.
     *
     * @return ghost type
     */
    public int getType();

    /**
     * Returns the current position as a string "x,y".
     *
     * @param code not used in this implementation
     * @return position string
     */
    public String getPos(int code);

    /**
     * Returns the remaining time (in seconds) that the ghost is eatable.
     *
     * @param code not used in this implementation
     * @return remaining eatable time in seconds, 0 if not eatable
     */
    public double remainTimeAsEatable(int code);

    /**
     * Returns the current status of the ghost.
     *
     * @return ghost status (e.g., PLAY)
     */
    public int getStatus();
}
