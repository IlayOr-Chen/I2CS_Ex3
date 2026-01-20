package server;

import utils.Index2D;

import java.util.Random;

public class Ghost implements GhostCL {
    private int type;
    private Index2D pos;
    private boolean eatable;
    private long eatableUntil;
    private int status;

    /**
     * Constructs a Ghost of a given type at a starting position.
     *
     * @param type the type of ghost
     * @param start starting position
     */
    public Ghost(int type, Index2D start) {
        this.type = type;
        this.pos = start;
        this.eatable = false;
        this.status = PLAY;
    }

    /**
     * Makes the ghost eatable until a specific time.
     *
     * @param until timestamp in milliseconds when eatable mode ends
     */
    public void setEatable(long until) {
        this.eatable = true;
        this.eatableUntil = until;
    }

    /**
     * Checks if the ghost is currently eatable.
     *
     * @return true if eatable and time has not expired, false otherwise
     */
    public boolean isEatable() {
        return eatable && System.currentTimeMillis() < eatableUntil;
    }

    /**
     * Resets the ghost to a starting position and non-eatable state.
     *
     * @param start new starting position
     */
    public void reset(Index2D start) {
        pos = start;
        eatable = false;
    }

    /**
     * Moves the ghost randomly to an adjacent free cell.
     *
     * @param map the current game map
     */
    public void moveRandom(GameMap map) {
        int[] dirs = {Game.UP,Game.LEFT,Game.DOWN,Game.RIGHT};
        Random r = new Random();

        for (int i = 0; i < dirs.length; i++) {
            int d = dirs[r.nextInt(dirs.length)];
            Index2D nextPixel = map.step(pos, d);
            if(map.isFree(nextPixel)) {
                pos = nextPixel;
                break;
            }
        }
    }

    /**
     * Returns the ghost type.
     *
     * @return ghost type
     */
    @Override
    public int getType() {
        return type;
    }

    /**
     * Returns the current position as a string "x,y".
     *
     * @param code not used in this implementation
     * @return position string
     */
    @Override
    public String getPos(int code) {
        return pos.getX() + "," + pos.getY();
    }

    /**
     * Returns the remaining time (in seconds) that the ghost is eatable.
     *
     * @param code not used in this implementation
     * @return remaining eatable time in seconds, 0 if not eatable
     */
    @Override
    public double remainTimeAsEatable(int code) {
        if(!isEatable())
            return 0;
        return (eatableUntil - System.currentTimeMillis()) / 1000.0;
    }

    /**
     * Returns the current status of the ghost.
     *
     * @return ghost status (e.g., PLAY)
     */
    @Override
    public int getStatus() {
        return status;
    }

    /**
     * Returns the current position as an Index2D object.
     *
     * @return ghost position
     */
    public Index2D getPos2D() {
        return pos;
    }
}